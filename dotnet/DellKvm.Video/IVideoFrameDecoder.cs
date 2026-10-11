namespace DellKvm.Video;

/// <summary>
/// Decodes one complete frame from the BMC video protocol.
///
/// The implementation will be ported from the original Java
/// video package. This interface deliberately contains no
/// Windows Forms dependencies.
/// </summary>
public interface IVideoFrameDecoder
{
    DecodedVideoFrame Decode(ReadOnlyMemory<byte> frameData);

    void Reset();
}

public sealed record DecodedVideoFrame(
    int Width,
    int Height,
    int Stride,
    byte[] PixelData);
    