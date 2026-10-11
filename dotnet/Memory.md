# DellKvm.NET — Project Memory

## Project purpose

Port the Dell/AMI Java KVM client used by the Dell C6100
BMC to C# and .NET 8 or later.

The application will be a Windows Forms desktop application.
The initial goal is to restore the functionality of the old
Java Web Start KVM client without requiring a legacy browser
or Java Web Start installation.

The user maintains the original server and has the Java source
code for the original client.

## Development preferences

- Target .NET 8 or later.
- Use C# and Windows Forms.
- Keep protocol, video, input, virtual media, and UI code
  in separate projects.
- Prefer straightforward, idiomatic C# over Java-style
  class structures and unnecessary abstractions.
- Provide complete source files with exact repository paths.
- Provide Markdown as literal Markdown source, not rendered
  documentation.
- Virtual-media image readers/writers can initially be stubs.
- Native hardware access can be stubbed where necessary.
- The user copies the supplied files into the repository and
  builds/tests locally.
- Do not claim code has been tested unless it has actually
  been executed.

## Repository

GitHub repository:
https://github.com/shawty/dellkvm

.NET project root:
dotnet/

Original Java source:
src/main/java/com/ami/

## Solution

The user builds the solution using:

    dotnet build DellKvm.slnx
    dotnet test DellKvm.slnx

The solution currently builds successfully on .NET 8.

## Projects

- DellKvm.Core
- DellKvm.Protocol
- DellKvm.Video
- DellKvm.Input
- DellKvm.VirtualMedia
- DellKvm.WinForms
- DellKvm.Tests

## Implemented so far

### Core and transport

- Existing connection configuration/model classes.
- TCP connection abstraction.
- IVTP packet/header serialization and parsing.
- Exact-length TCP reads.
- Packet write serialization.
- Initial WinForms connection window.

### Protocol

- Video-session validation request builder.
- Validation request uses message type 34 and a 16-byte
  session-token field.
- Video fragment assembler for message type 5.
- Initial receive loop for message types 5, 32 and 48.
- Video-session state model.
- Initial session validation handler for message type 35.
- Initial bandwidth configuration packet builder.

### Video

- Initial IVideoFrameDecoder abstraction.
- DecodedVideoFrame model.
- Actual decoder port remains outstanding.

### Input

- Initial IKvmInputSender abstraction.
- Keyboard and mouse encoding remains outstanding.

### Virtual media

- ISO/floppy media-kind model.
- IImageMediaProvider abstraction.
- StubImageMediaProvider intentionally does not open files.
- Actual virtual-media protocol and image handling remain
  outstanding.

### UI

- Basic Windows Forms connection UI.
- It is not yet a functional remote-console viewer.

## Current test status

Last reported local test run:

    Total: 16
    Passed: 16
    Failed: 0

Last reported build:
Successful.

These results were reported by the user on 2026-10-11.
They have not been independently reproduced by the assistant.

## Important protocol caveat

The receive loop and session implementation are preliminary.
Passing unit tests do not establish that the protocol assumptions
have been verified against a real BMC.

In particular, recheck the following against the Java source:

- Video fragment framing and fragment-number semantics.
- Bandwidth-detection message type 32.
- Cursor message type 48.
- Validation response type 35 and its result byte.
- Bandwidth configuration message type 7.
- Exact session startup and shutdown sequence.

Do not assume the initial implementations are wire-compatible
until verified against the original Java implementation or
a real BMC session.

## Next planned work

1. Integrate KvmVideoSession with KvmReceiveLoop.
2. Ensure validation responses reach the session state machine.
3. Implement cancellation and clean disconnect handling.
4. Add a loopback TCP integration test.
5. Verify the handshake and configuration sequence against Java.
6. Port the video reader and decoder.
7. Port keyboard/mouse encoding.
8. Implement virtual-media protocol support.
9. Replace image-provider stubs with the user's own implementation.
10. Complete the WinForms remote-console viewer.

## Working rules

- Work in small, buildable batches.
- Include complete files rather than partial fragments when practical.
- Include tests for protocol behaviour.
- Ask the user to run the build and tests before proceeding.
- Fix failures before adding more features.
- Do not assume the public repository can be modified directly.
- The user will push changes to GitHub.
