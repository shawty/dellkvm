using DellKvm.Protocol;

namespace DellKvm.Tests;

public sealed class VideoFragmentAssemblerTests
{
  [Fact]
public void Append_ReturnsNullUntilFinalFragment()
{
    var assembler = new VideoFragmentAssembler();

    var first = CreateFragment(0x0000, "ABC");
    var second = CreateFragment(0x0001, "DEF");
    var final = CreateFragment(0x8002, "GHI");

    Assert.Null(assembler.Append(first));
    Assert.Equal(3, assembler.BufferedBytes);

    Assert.Null(assembler.Append(second));
    Assert.Equal(6, assembler.BufferedBytes);

    var result = assembler.Append(final);

    Assert.NotNull(result);
    Assert.Equal(0, assembler.BufferedBytes);

    Assert.Equal(
        "ABCDEFGHI",
        System.Text.Encoding.ASCII.GetString(result!));
}
  
//    [Fact]
//    public void Append_ReturnsNullUntilFinalFragment()
//    {
//        var assembler = new VideoFragmentAssembler();
//
//        var first = CreateFragment(0x0000, "ABC");
//        var second = CreateFragment(0x0001, "DEF");
//
//        Assert.Null(assembler.Append(first));
//        Assert.Equal(3, assembler.BufferedBytes);
//
//        var final = CreateFragment(0x8002, "GHI");
//
//        var result = assembler.Append(final);
//
//        Assert.NotNull(result);
//        Assert.Equal("ABCDEFGHI", System.Text.Encoding.ASCII.GetString(result!));
//        Assert.Equal(0, assembler.BufferedBytes);
//    }

    [Fact]
    public void Append_SupportsSingleFragmentFrame()
    {
        var assembler = new VideoFragmentAssembler();

        var packet = CreateFragment(0x8000, "FRAME");

        var result = assembler.Append(packet);

        Assert.Equal(
            "FRAME",
            System.Text.Encoding.ASCII.GetString(result!));
    }

    [Fact]
    public void Append_NewFirstFragmentResetsPreviousFrame()
    {
        var assembler = new VideoFragmentAssembler();

        Assert.Null(assembler.Append(
            CreateFragment(0x0000, "OLD")));

        var result = assembler.Append(
            CreateFragment(0x8000, "NEW"));

        Assert.Equal(
            "NEW",
            System.Text.Encoding.ASCII.GetString(result!));
    }

    [Fact]
    public void Append_RejectsNonVideoPacket()
    {
        var assembler = new VideoFragmentAssembler();

        var packet = IVTPPacket.Create(32, new byte[] { 1, 2 });

        Assert.Throws<ArgumentException>(
            () => assembler.Append(packet));
    }

    [Fact]
    public void Append_RejectsMissingFragmentNumber()
    {
        var assembler = new VideoFragmentAssembler();

        var packet = IVTPPacket.Create(5, Array.Empty<byte>());

        Assert.Throws<InvalidDataException>(
            () => assembler.Append(packet));
    }

    private static IVTPPacket CreateFragment(
        ushort fragmentNumber,
        string data)
    {
        var textBytes = System.Text.Encoding.ASCII.GetBytes(data);
        var payload = new byte[textBytes.Length + 2];

        payload[0] = (byte)(fragmentNumber & 0xFF);
        payload[1] = (byte)(fragmentNumber >> 8);

        textBytes.CopyTo(payload, 2);

        return IVTPPacket.Create(5, payload);
    }
}
