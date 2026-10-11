using System.Text;

namespace DellKvm.Protocol;

public static class VideoSessionProtocol
{
    public const byte ValidateVideoSession = 34;
    public const byte ValidateVideoSessionResponse = 35;
    public const int SessionTokenFieldLength = 16;

    public static IVTPPacket CreateValidationRequest(
        string sessionToken)
    {
        ArgumentException.ThrowIfNullOrEmpty(sessionToken);

        if (sessionToken.Any(c => c > 0x7F))
        {
            throw new ArgumentException(
                "The session token must contain ASCII characters.",
                nameof(sessionToken));
        }

        var tokenBytes = Encoding.ASCII.GetBytes(sessionToken);

        if (tokenBytes.Length > SessionTokenFieldLength)
        {
            throw new ArgumentException(
                $"The session token cannot exceed " +
                $"{SessionTokenFieldLength} bytes.",
                nameof(sessionToken));
        }

        var payload = new byte[SessionTokenFieldLength];

        tokenBytes.CopyTo(payload, 0);

        return IVTPPacket.Create(
            ValidateVideoSession,
            payload);
    }
}
