namespace DellKvm.Protocol;

public sealed record IVTPPacket(
    IVTPPacketHeader Header,
    byte[] Payload)
{
    public static IVTPPacket Create(
        byte type,
        ReadOnlySpan<byte> payload,
        short status = 0)
    {
        return new IVTPPacket(
            new IVTPPacketHeader(
                type,
                payload.Length,
                status),
            payload.ToArray());
    }
}
