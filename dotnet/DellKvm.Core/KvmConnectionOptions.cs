namespace DellKvm.Core;

public sealed record KvmConnectionOptions
{
    public required string Host { get; init; }

    public required int Port { get; init; }

    public required string SessionToken { get; init; }

    public void Validate()
    {
        if (string.IsNullOrWhiteSpace(Host))
            throw new ArgumentException(
                "A BMC host name or IP address is required.",
                nameof(Host));

        if (Port is < 1 or > 65535)
            throw new ArgumentOutOfRangeException(
                nameof(Port),
                "Port must be between 1 and 65535.");

        if (string.IsNullOrEmpty(SessionToken))
            throw new ArgumentException(
                "A session token is required.",
                nameof(SessionToken));
    }
}
