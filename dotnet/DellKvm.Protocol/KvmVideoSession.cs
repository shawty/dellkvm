using System.Buffers.Binary;

namespace DellKvm.Protocol;

/// <summary>
/// Manages validation of a Dell/AMI video session.
///
/// The caller owns the TCP connection and delivers incoming
/// packets to ProcessIncomingPacketAsync.
/// </summary>
public sealed class KvmVideoSession
{
    public const byte ValidateVideoSessionRequest = 34;
    public const byte ValidateVideoSessionResponse = 35;
    public const byte SetBandwidthMessage = 7;

    public const int Bandwidth100Mbps = 13_107_200;

    private readonly string _sessionToken;

    private readonly Func<
        IVTPPacket,
        CancellationToken,
        Task> _sendPacketAsync;

    public KvmVideoSession(
        string sessionToken,
        Func<IVTPPacket, CancellationToken, Task> sendPacketAsync)
    {
        ArgumentException.ThrowIfNullOrEmpty(sessionToken);
        ArgumentNullException.ThrowIfNull(sendPacketAsync);

        _sessionToken = sessionToken;
        _sendPacketAsync = sendPacketAsync;
    }

    public KvmVideoSessionState State { get; private set; } =
        KvmVideoSessionState.Disconnected;

    public async Task StartAsync(
        CancellationToken cancellationToken = default)
    {
        if (State != KvmVideoSessionState.Disconnected)
        {
            throw new InvalidOperationException(
                $"Cannot start a session while its state is {State}.");
        }

        State = KvmVideoSessionState.Connecting;

        try
        {
            var request =
                VideoSessionProtocol.CreateValidationRequest(
                    _sessionToken);

            State = KvmVideoSessionState.Validating;

            await _sendPacketAsync(
                request,
                cancellationToken).ConfigureAwait(false);
        }
        catch
        {
            State = KvmVideoSessionState.Faulted;
            throw;
        }
    }

    /// <summary>
    /// Processes an incoming packet.
    ///
    /// Packets unrelated to session validation are left to
    /// the caller's normal receive-loop dispatch.
    /// </summary>
    public async Task ProcessIncomingPacketAsync(
        IVTPPacket packet,
        CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(packet);

        if (State != KvmVideoSessionState.Validating)
        {
            return;
        }

        if (packet.Header.Type != ValidateVideoSessionResponse)
        {
            return;
        }

        try
        {
            if (packet.Payload.Length < 1)
            {
                throw new InvalidDataException(
                    "The video-session response contains no result byte.");
            }

            // The Java client treats zero as rejection and
            // a nonzero value as acceptance.
            if (packet.Payload[0] == 0)
            {
                State = KvmVideoSessionState.Rejected;
                return;
            }

            // Configure bandwidth after acceptance. The original
            // Java setter is called before its redirection flag
            // is set, preventing that setter from sending anything.
            var bandwidthPacket = CreateBandwidthPacket(
                Bandwidth100Mbps);

            await _sendPacketAsync(
                bandwidthPacket,
                cancellationToken).ConfigureAwait(false);

            State = KvmVideoSessionState.Connected;
        }
        catch
        {
            State = KvmVideoSessionState.Faulted;
            throw;
        }
    }

    public static IVTPPacket CreateBandwidthPacket(int bandwidth)
    {
        var payload = new byte[sizeof(int)];

        BinaryPrimitives.WriteInt32LittleEndian(
            payload,
            bandwidth);

        return IVTPPacket.Create(
            SetBandwidthMessage,
            payload);
    }

    public void Reset()
    {
        State = KvmVideoSessionState.Disconnected;
    }
}
