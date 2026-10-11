# Next Session — DellKvm.NET

## Starting point

Read `Memory.md` first.

The user has pushed the current .NET implementation to:
https://github.com/shawty/dellkvm/tree/main/dotnet

The solution targets .NET 8 or later.

## Last verified by user

- `dotnet build DellKvm.slnx` — successful.
- `dotnet test DellKvm.slnx` — 16 passed, 0 failed.

## First task

Integrate `KvmVideoSession` into `KvmReceiveLoop`.

Requirements:

1. Use one TCP receive loop only.
2. Route message type 35 to the session state machine.
3. Preserve dispatch of unrelated packets.
4. Handle cancellation and connection closure cleanly.
5. Add tests before attempting live BMC connections.

## Before changing protocol code

Inspect the current repository files and the original Java
implementations. Verify packet layouts and message ordering
rather than relying solely on earlier assumptions.

## Output format

For every change, supply:

- Exact file path.
- Complete source code where practical.
- Any files to remove or rename.
- Build and test commands.
- A concise explanation of the change.

Provide Markdown files as raw Markdown source.
