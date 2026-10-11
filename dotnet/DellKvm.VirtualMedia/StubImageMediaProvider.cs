namespace DellKvm.VirtualMedia;

/// <summary>
/// Explicit placeholder for the image-access implementation.
/// It does not open files or claim to provide virtual media.
/// </summary>
public sealed class StubImageMediaProvider : IImageMediaProvider
{
    public ValueTask<Stream> OpenReadAsync(
        string imagePath,
        ImageMediaKind mediaKind,
        CancellationToken cancellationToken = default)
    {
        ArgumentException.ThrowIfNullOrWhiteSpace(imagePath);

        cancellationToken.ThrowIfCancellationRequested();

        throw new NotSupportedException(
            "Image access has not been implemented yet. " +
            "Replace StubImageMediaProvider with the required " +
            "image reader implementation.");
    }
}
