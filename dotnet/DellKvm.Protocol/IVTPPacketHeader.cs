using System.Buffers.Binary;

namespace DellKvm.Protocol;

/// <summary>
/// Header used by the AMI IVTP protocol.
///
/// Wire format:
///   Byte 0:    Message type
///   Bytes 1-4: Payload length, little-endian Int32
///   Bytes 5-6: Status, little-endian Int16
/// </summary>
public readonly record struct IVTPPacketHeader(
    byte Type,
    int PayloadLength,
    short Status)
{
    public const int WireSize = 7;

    public byte[] Serialize()
    {
        if (PayloadLength < 0)
        {
            throw new ArgumentOutOfRangeException(
                nameof(PayloadLength));
        }

        var buffer = new byte[WireSize];

        buffer[0] = Type;

        BinaryPrimitives.WriteInt32LittleEndian(
            buffer.AsSpan(1, 4),
            PayloadLength);

        BinaryPrimitives.WriteInt16LittleEndian(
            buffer.AsSpan(5, 2),
            Status);

        return buffer;
    }

    public static IVTPPacketHeader Parse(
        ReadOnlySpan<byte> buffer)
    {
        if (buffer.Length < WireSize)
        {
            throw new ArgumentException(
                $"An IVTP header requires {WireSize} bytes.",
                nameof(buffer));
        }

        var type = buffer[0];

        var payloadLength =
            BinaryPrimitives.ReadInt32LittleEndian(
                buffer.Slice(1, 4));

        var status =
            BinaryPrimitives.ReadInt16LittleEndian(
                buffer.Slice(5, 2));

        if (payloadLength < 0)
        {
            throw new InvalidDataException(
                "The IVTP payload length cannot be negative.");
        }

        return new IVTPPacketHeader(
            type,
            payloadLength,
            status);
    }
}
