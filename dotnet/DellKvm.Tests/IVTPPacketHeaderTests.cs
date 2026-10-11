using DellKvm.Protocol;

namespace DellKvm.Tests;

public sealed class IVTPPacketHeaderTests
{
    [Fact]
    public void Serialize_ProducesSevenByteHeader()
    {
        var header = new IVTPPacketHeader(
            Type: 34,
            PayloadLength: 16,
            Status: 0);

        var bytes = header.Serialize();

        Assert.Equal(7, bytes.Length);

        Assert.Equal(
            new byte[]
            {
                34,             // Message type
                16, 0, 0, 0,    // Payload length: little-endian
                0, 0            // Status: little-endian
            },
            bytes);
    }

    [Fact]
    public void Parse_RestoresHeaderFields()
    {
        var original = new IVTPPacketHeader(
            Type: 35,
            PayloadLength: 1024,
            Status: 2);

        var parsed = IVTPPacketHeader.Parse(
            original.Serialize());

        Assert.Equal(original, parsed);
    }

    [Fact]
    public void Parse_RejectsShortHeader()
    {
        var bytes = new byte[6];

        Assert.Throws<ArgumentException>(
            () => IVTPPacketHeader.Parse(bytes));
    }

    [Fact]
    public void Parse_RejectsNegativePayloadLength()
    {
        var bytes = new byte[]
        {
            34,
            0xFF, 0xFF, 0xFF, 0xFF,
            0, 0
        };

        Assert.Throws<InvalidDataException>(
            () => IVTPPacketHeader.Parse(bytes));
    }

    [Fact]
    public void ValidationRequest_ContainsPaddedToken()
    {
        var packet =
            VideoSessionProtocol.CreateValidationRequest("ABC123");

        Assert.Equal((byte)34, packet.Header.Type);
        Assert.Equal(16, packet.Header.PayloadLength);

        Assert.Equal(
            new byte[]
            {
                65, 66, 67, 49, 50, 51,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },
            packet.Payload);
    }
}
