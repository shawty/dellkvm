package com.ami.iusb.protocol;

import com.ami.iusb.RedirProtocolException;
import com.ami.iusb.RedirectionException;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class IUSBSCSI extends RedirPacket {
    public int connectionStatus = -1;
    public ByteBuffer data;
    public int dataLen;
    public String m_otherIP;
    public int opcode;
    private boolean preBuffered;

    public IUSBSCSI(IUSBHeader iUSBHeader) {
        this.header = iUSBHeader;
        this.dataLen = (int) iUSBHeader.dataPacketLen;
    }

    public IUSBSCSI(ByteBuffer byteBuffer, boolean z) throws RedirProtocolException {
        this.preBuffered = z;
        this.header = new IUSBHeader();
        this.header.read(byteBuffer);
        ((IUSBHeader) this.header).dataPacketLen = (long) (byteBuffer.limit() - this.header.headerLen);
        this.dataLen = (int) ((IUSBHeader) this.header).dataPacketLen;
        this.data = byteBuffer.slice();
    }

    @Override // com.ami.iusb.protocol.RedirPacket
    public void writePacket(ByteBuffer byteBuffer) {
        ((IUSBHeader) this.header).direction = USBMessage.IUSB_FROM_REMOTE;
        this.header.write(byteBuffer);
        if (this.preBuffered) {
            byteBuffer.limit(this.dataLen + 32);
            byteBuffer.position(byteBuffer.limit());
            return;
        }
        byteBuffer.put(this.data);
    }

    @Override // com.ami.iusb.protocol.RedirPacket
    public void readPacket(ByteBuffer byteBuffer) throws RedirectionException {
        this.header.read(byteBuffer);
        readData(byteBuffer);
    }

    @Override // com.ami.iusb.protocol.RedirPacket
    public void readData(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[24];
        this.data = ByteBuffer.allocate(this.dataLen);
        int limit = byteBuffer.limit();
        byteBuffer.limit(byteBuffer.position() + this.dataLen);
        this.data.put(byteBuffer);
        byteBuffer.limit(limit);
        this.opcode = this.data.get(9) & 255;
        if (this.opcode != 241 || this.dataLen <= 30) {
            this.connectionStatus = -1;
            return;
        }
        this.connectionStatus = this.data.get(30);
        try {
            this.data.position(31);
            this.data.get(bArr);
            this.m_otherIP = new String(bArr).trim();
        } catch (BufferUnderflowException e) {
            System.err.println(e.getMessage());
            this.m_otherIP = new String("");
        }
    }
}
