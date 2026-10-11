DellKvm.NET

A C#/.NET implementation of the Dell/AMI KVM client.

Requirements
.NET 8 SDK or later
Windows for the Windows Forms application
Projects
DellKvm.Core — shared configuration and models.
DellKvm.Protocol — IVTP packet framing and TCP transport.
DellKvm.Video — video decoding interfaces and decoder implementation.
DellKvm.Input — keyboard and mouse input handling.
DellKvm.VirtualMedia — virtual-media protocol and image-provider abstractions.
DellKvm.WinForms — Windows desktop application.
DellKvm.Tests — automated tests.
Build

From this directory:

dotnet restore
dotnet build DellKvm.sln
dotnet test DellKvm.sln

Current status

The initial implementation establishes the project structure, IVTP packet framing, TCP transport, and video-session validation request.

The complete KVM session lifecycle, incoming video processing, input encoding, and virtual-media protocol remain to be implemented.

The virtual-media image provider is currently a deliberate stub.