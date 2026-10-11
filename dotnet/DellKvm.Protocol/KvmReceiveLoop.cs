using System.Diagnostics;

namespace DellKvm.Protocol;

/// <summary>
/// Reads incoming IVTP messages and dispatches them according
/// to the message types used by the original AMI KVM client.
///
/// Run this on a background task. Event handlers execute on
/// the calling receive-loop thread.
/// </summary>
public sealed class KvmReceiveLoop
{
    public const byte VideoFragmentType = 5;
    public const byte BandwidthDetectionType = 32;
    public const byte CursorPacketType = 48;

    private readonly VideoFragmentAssembler _videoAssembler = new();

    public event Action<ReadOnlyMemory<byte>>? VideoFrameReceived;

    public event Action<IVTPPacket>? ControlPacketReceived;

    public event Action<IVTPPacket>? CursorPacketReceived;

    public event Action<TimeSpan>? BandwidthDetectionCompleted;

    public async Task RunAsync(
        KvmTcpConnection connection,
        CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(connection);

        _videoAssembler.Reset();

        while (!cancellationToken.IsCancellationRequested)
        {
            var packet = await connection.ReadPacketAsync(
                cancellationToken).ConfigureAwait(false);

            switch (packet.Header.Type)
            {
                case VideoFragmentType:
                    ProcessVideoFragment(packet);
                    break;

                case BandwidthDetectionType:
                    await ProcessBandwidthDetectionAsync(
                        connection,
                        packet,
                        cancellationToken).ConfigureAwait(false);
                    break;

                case CursorPacketType:
                    CursorPacketReceived?.Invoke(packet);
                    break;

                default:
                    ControlPacketReceived?.Invoke(packet);
                    break;
            }
        }
    }

    private void ProcessVideoFragment(IVTPPacket packet)
    {
        var completeFrame = _videoAssembler.Append(packet);

        if (completeFrame is not null)
        {
            VideoFrameReceived?.Invoke(completeFrame);
        }
    }

    private async Task ProcessBandwidthDetectionAsync(
        KvmTcpConnection connection,
        IVTPPacket firstPacket,
        CancellationToken cancellationToken)
    {
        // The original Java NullReader interprets the header
        // status as the number of data blocks to consume.
        // The first block has already been read with the packet.
        var blockCount = Math.Max(1, (int)firstPacket.Header.Status);

        var timer = Stopwatch.StartNew();

        for (var i = 1; i < blockCount; i++)
        {
            _ = await connection.ReadPayloadAsync(
                firstPacket.Header.PayloadLength,
                cancellationToken).ConfigureAwait(false);
        }

        timer.Stop();

        BandwidthDetectionCompleted?.Invoke(timer.Elapsed);
    }
}
