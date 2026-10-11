using System.Net.Sockets;

namespace DellKvm.Protocol;

public sealed class KvmTcpConnection : IAsyncDisposable
{
    private const int DefaultMaximumPayload = 4_000_000;

    private readonly TcpClient _client;
    private readonly NetworkStream _stream;
    private readonly SemaphoreSlim _writeLock = new(1, 1);
    private readonly int _maximumPayload;

    private KvmTcpConnection(
        TcpClient client,
        int maximumPayload)
    {
        _client = client;
        _stream = client.GetStream();
        _maximumPayload = maximumPayload;
    }

    public bool Connected => _client.Connected;

    public static async Task<KvmTcpConnection> ConnectAsync(
        string host,
        int port,
        CancellationToken cancellationToken = default,
        int maximumPayload = DefaultMaximumPayload)
    {
        ArgumentException.ThrowIfNullOrWhiteSpace(host);

        if (port is < 1 or > 65535)
            throw new ArgumentOutOfRangeException(nameof(port));

        if (maximumPayload < 0)
            throw new ArgumentOutOfRangeException(
                nameof(maximumPayload));

        var client = new TcpClient
        {
            NoDelay = true
        };

        try
        {
            await client.ConnectAsync(
                host,
                port,
                cancellationToken).ConfigureAwait(false);

            return new KvmTcpConnection(
                client,
                maximumPayload);
        }
        catch
        {
            client.Dispose();
            throw;
        }
    }

    public async Task<IVTPPacket> ReadPacketAsync(
        CancellationToken cancellationToken = default)
    {
        var headerBuffer = new byte[IVTPPacketHeader.WireSize];

        await _stream.ReadExactlyAsync(
            headerBuffer,
            cancellationToken).ConfigureAwait(false);

        var header = IVTPPacketHeader.Parse(headerBuffer);

        if (header.PayloadLength > _maximumPayload)
        {
            throw new InvalidDataException(
                $"IVTP payload length {header.PayloadLength} " +
                $"exceeds the configured maximum of {_maximumPayload}.");
        }

        var payload = new byte[header.PayloadLength];

        if (payload.Length > 0)
        {
            await _stream.ReadExactlyAsync(
                payload,
                cancellationToken).ConfigureAwait(false);
        }

        return new IVTPPacket(header, payload);
    }

    public async Task WritePacketAsync(
        IVTPPacket packet,
        CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(packet);

        if (packet.Payload.Length != packet.Header.PayloadLength)
        {
            throw new InvalidDataException(
                "The IVTP header payload length does not match " +
                "the actual payload.");
        }

        if (packet.Payload.Length > _maximumPayload)
        {
            throw new InvalidDataException(
                "The packet exceeds the configured payload limit.");
        }

        var headerBytes = packet.Header.Serialize();

        await _writeLock.WaitAsync(cancellationToken)
            .ConfigureAwait(false);

        try
        {
            await _stream.WriteAsync(
                headerBytes,
                cancellationToken).ConfigureAwait(false);

            if (packet.Payload.Length > 0)
            {
                await _stream.WriteAsync(
                    packet.Payload,
                    cancellationToken).ConfigureAwait(false);
            }

            await _stream.FlushAsync(cancellationToken)
                .ConfigureAwait(false);
        }
        finally
        {
            _writeLock.Release();
        }
    }

    public async ValueTask DisposeAsync()
    {
        await _stream.DisposeAsync().ConfigureAwait(false);
        _client.Dispose();
        _writeLock.Dispose();
    }
}
