namespace DellKvm.Protocol;

public enum KvmVideoSessionState
{
    Disconnected, // No validation Attempt is in progress
    Connecting, // The initial request is being sent
    Validating, // Awaiting the BMC's response
    Connected,  // The BMC accepted the session and the initial BW config was sent
    Rejected,  // the BMC rejected the session token
    Faulted // an I/O or protocol error prevented the handshake from compleating.
}
