using System.Buffers.Binary;

namespace DellKvm.Protocol;

/// <summary>
/// Reassembles IVTP type-5 video fragments into complete frames.
///
/// The fragment number is a little-endian 16-bit value:
/// - Bits 0-14 contain the fragment number.
/// - Bit 15 indicates the final fragment.
/// </summary>
public sealed class VideoFragmentAssembler
{
    public const int MaximumFrameSize = 3_840_000;

    private const ushort FinalFragmentFlag = 0x8000;
    private const ushort FragmentNumberMask = 0x7FFF;

    private readonly MemoryStream _frame = new();

    public int BufferedBytes => checked((int)_frame.Length);

    public byte[]? Append(IVTPPacket packet)
    {
        ArgumentNullException.ThrowIfNull(packet);

        if (packet.Header.Type != 5)
        {
            throw new ArgumentException(
                "Only IVTP type-5 video fragments are accepted.",
                nameof(packet));
        }

        if (packet.Payload.Length < 2)
        {
            throw new InvalidDataException(
                "A video fragment must contain its two-byte fragment number.");
        }

        var fragmentNumber =
            BinaryPrimitives.ReadUInt16LittleEndian(
                packet.Payload.AsSpan(0, 2));

        var sequence =
            (ushort)(fragmentNumber & FragmentNumberMask);

        var isFinal =
            (fragmentNumber & FinalFragmentFlag) != 0;

        // The Java implementation starts a new frame when
        // the low 15 bits of the fragment number are zero.
        if (sequence == 0)
        {
            _frame.SetLength(0);
        }

        var data = packet.Payload.AsSpan(2);

        if (data.Length > MaximumFrameSize - _frame.Length)
        {
            _frame.SetLength(0);

            throw new InvalidDataException(
                $"Assembled video frame exceeds {MaximumFrameSize} bytes.");
        }

        _frame.Write(data);

        if (!isFinal)
            return null;

        var completeFrame = _frame.ToArray();

        _frame.SetLength(0);

        return completeFrame;
    }

    public void Reset()
    {
        _frame.SetLength(0);
    }
}
