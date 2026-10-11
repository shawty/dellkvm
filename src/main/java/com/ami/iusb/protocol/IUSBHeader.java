package com.ami.iusb.protocol;

import com.ami.iusb.RedirProtocolException;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.VideoSettings;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class IUSBHeader extends RedirHeader {
    public static final int HEADER_LEN = 32;
    private static final int IUSB_MAJOR = 1;
    private static final int IUSB_MINOR = 0;
    public byte[] clientData = new byte[2];
    public long dataPacketLen;
    public int deviceNumber;
    public int deviceType;
    public int direction;
    public int headerChecksum;
    public int interfaceNumber;
    public int major;
    public int minor;
    public int packetHeaderLen;
    public int protocol;
    public byte[] reserved = new byte[4];
    public long sequenceNumber;
    public int serverCaps;
    public String signature;

    public IUSBHeader() {
        this.headerLen = 32;
    }

    public static IUSBHeader createKeyboardHeader(int i) {
        IUSBHeader iUSBHeader = new IUSBHeader();
        iUSBHeader.signature = "IUSB    ";
        iUSBHeader.packetHeaderLen = 32;
        iUSBHeader.major = 1;
        iUSBHeader.minor = 0;
        iUSBHeader.sequenceNumber = 0;
        iUSBHeader.direction = USBMessage.IUSB_FROM_REMOTE;
        iUSBHeader.dataPacketLen = (long) i;
        iUSBHeader.deviceType = 48;
        iUSBHeader.deviceNumber = 2;
        iUSBHeader.interfaceNumber = 0;
        iUSBHeader.protocol = 16;
        return iUSBHeader;
    }

    public static IUSBHeader createMouseHeader(int i) {
        IUSBHeader iUSBHeader = new IUSBHeader();
        iUSBHeader.signature = "IUSB    ";
        iUSBHeader.packetHeaderLen = 32;
        iUSBHeader.major = 1;
        iUSBHeader.minor = 0;
        iUSBHeader.sequenceNumber = 0;
        iUSBHeader.direction = USBMessage.IUSB_FROM_REMOTE;
        iUSBHeader.dataPacketLen = (long) i;
        iUSBHeader.deviceType = 49;
        iUSBHeader.deviceNumber = 2;
        iUSBHeader.interfaceNumber = 1;
        iUSBHeader.protocol = 32;
        return iUSBHeader;
    }

    public static IUSBHeader createCDROMHeader(int i) {
        IUSBHeader iUSBHeader = new IUSBHeader();
        iUSBHeader.signature = "IUSB    ";
        iUSBHeader.packetHeaderLen = 32;
        iUSBHeader.major = 1;
        iUSBHeader.minor = 0;
        iUSBHeader.sequenceNumber = 0;
        iUSBHeader.direction = USBMessage.IUSB_FROM_REMOTE;
        iUSBHeader.dataPacketLen = (long) i;
        iUSBHeader.deviceType = 5;
        iUSBHeader.deviceNumber = 0;
        iUSBHeader.interfaceNumber = 0;
        iUSBHeader.protocol = 1;
        return iUSBHeader;
    }

    @Override // com.ami.iusb.protocol.RedirHeader
    public void read(ByteBuffer byteBuffer) throws RedirProtocolException, BufferUnderflowException {
        byte[] bArr = new byte[8];
        byteBuffer.get(bArr);
        this.signature = new String(bArr);
        if (!this.signature.equals("IUSB    ")) {
            Debug.out.dump(bArr);
            throw new RedirProtocolException("Invalid header signature");
        }
        this.major = byteBuffer.get() & 255;
        this.minor = byteBuffer.get() & 255;
        this.packetHeaderLen = byteBuffer.get() & 255;
        this.headerChecksum = byteBuffer.get() & 255;
        this.dataPacketLen = ((long) byteBuffer.getInt()) & -1;
        this.serverCaps = byteBuffer.get() & 255;
        this.deviceType = byteBuffer.get() & 255;
        this.protocol = byteBuffer.get() & 255;
        this.direction = byteBuffer.get() & 255;
        this.deviceNumber = byteBuffer.get() & 255;
        this.interfaceNumber = byteBuffer.get() & 255;
        byteBuffer.get(this.clientData);
        this.sequenceNumber = ((long) byteBuffer.getInt()) & -1;
        byteBuffer.get(this.reserved);
    }

    @Override // com.ami.iusb.protocol.RedirHeader
    public void write(ByteBuffer byteBuffer) {
        int i = 0;
        byteBuffer.put(this.signature.getBytes());
        byteBuffer.put((byte) (this.major & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.minor & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.packetHeaderLen & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.headerChecksum & VideoSettings.GAIN_MAX));
        byteBuffer.putInt((int) (this.dataPacketLen & -1));
        byteBuffer.put((byte) (this.serverCaps & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.deviceType & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.protocol & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.direction & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.deviceNumber & VideoSettings.GAIN_MAX));
        byteBuffer.put((byte) (this.interfaceNumber & VideoSettings.GAIN_MAX));
        byteBuffer.put(this.clientData);
        byteBuffer.putInt((int) (this.sequenceNumber & -1));
        byteBuffer.put(this.reserved);
        for (int i2 = 0; i2 < byteBuffer.limit(); i2++) {
            i = ((byteBuffer.get(i2) & 255) + i) & VideoSettings.GAIN_MAX;
        }
        byteBuffer.put(11, (byte) (-((byte) (i & VideoSettings.GAIN_MAX))));
    }
}
