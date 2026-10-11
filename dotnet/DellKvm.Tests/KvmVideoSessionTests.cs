using DellKvm.Protocol;

namespace DellKvm.Tests;

public sealed class KvmVideoSessionTests
{
    [Fact]
    public async Task StartAsync_SendsValidationRequest()
    {
        var sent = new List<IVTPPacket>();

        var session = CreateSession(sent);

        await session.StartAsync();

        Assert.Equal(
            KvmVideoSessionState.Validating,
            session.State);

        var request = Assert.Single(sent);

        Assert.Equal((byte)34, request.Header.Type);
        Assert.Equal(16, request.Header.PayloadLength);

        Assert.Equal(
            "ABC123",
            System.Text.Encoding.ASCII
                .GetString(request.Payload)
                .TrimEnd('\0'));
    }

    [Fact]
    public async Task SuccessfulResponse_SendsBandwidthConfiguration()
    {
        var sent = new List<IVTPPacket>();
        var session = CreateSession(sent);

        await session.StartAsync();

        await session.ProcessIncomingPacketAsync(
            IVTPPacket.Create(35, new byte[] { 1 }));

        Assert.Equal(
            KvmVideoSessionState.Connected,
            session.State);

        Assert.Equal(2, sent.Count);

        var bandwidthPacket = sent[1];

        Assert.Equal((byte)7, bandwidthPacket.Header.Type);
        Assert.Equal(4, bandwidthPacket.Header.PayloadLength);

        Assert.Equal(
            new byte[] { 0x00, 0x00, 0xC8, 0x00 },
            bandwidthPacket.Payload);
    }

    [Fact]
    public async Task RejectedResponse_DoesNotConfigureBandwidth()
    {
        var sent = new List<IVTPPacket>();
        var session = CreateSession(sent);

        await session.StartAsync();

        await session.ProcessIncomingPacketAsync(
            IVTPPacket.Create(35, new byte[] { 0 }));

        Assert.Equal(
            KvmVideoSessionState.Rejected,
            session.State);

        Assert.Single(sent);
    }

    [Fact]
    public async Task UnrelatedPacket_DoesNotCompleteValidation()
    {
        var sent = new List<IVTPPacket>();
        var session = CreateSession(sent);

        await session.StartAsync();

        await session.ProcessIncomingPacketAsync(
            IVTPPacket.Create(23, new byte[] { 1 }));

        Assert.Equal(
            KvmVideoSessionState.Validating,
            session.State);

        Assert.Single(sent);
    }

    [Fact]
    public async Task EmptyResponse_FaultsSession()
    {
        var sent = new List<IVTPPacket>();
        var session = CreateSession(sent);

        await session.StartAsync();

        await Assert.ThrowsAsync<InvalidDataException>(
            () => session.ProcessIncomingPacketAsync(
                IVTPPacket.Create(35, Array.Empty<byte>())));

        Assert.Equal(
            KvmVideoSessionState.Faulted,
            session.State);
    }

    [Fact]
    public async Task Reset_ReturnsSessionToDisconnected()
    {
        var sent = new List<IVTPPacket>();
        var session = CreateSession(sent);

        await session.StartAsync();
        session.Reset();

        Assert.Equal(
            KvmVideoSessionState.Disconnected,
            session.State);
    }

    private static KvmVideoSession CreateSession(
        List<IVTPPacket> sent)
    {
        return new KvmVideoSession(
            "ABC123",
            (packet, _) =>
            {
                sent.Add(packet);
                return Task.CompletedTask;
            });
    }
}
