namespace DellKvm.VirtualMedia;

public enum ImageMediaKind
{
    Iso,
    Floppy
}

/// <summary>
/// Abstraction for supplying image data to virtual-media
/// protocol handlers.
///
/// The actual image reader/writer is intentionally left for
/// a later implementation.
/// </summary>
public interface IImageMediaProvider
{
    ValueTask<Stream> OpenReadAsync(
        string imagePath,
        ImageMediaKind mediaKind,
        CancellationToken cancellationToken = default);
}
