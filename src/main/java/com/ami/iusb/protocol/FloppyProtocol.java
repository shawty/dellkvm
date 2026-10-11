package com.ami.iusb.protocol;

import com.ami.iusb.RedirProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class FloppyProtocol extends RedirProtocol {
    @Override // com.ami.iusb.protocol.RedirProtocol
    public int getDataLen(ByteBuffer byteBuffer) throws RedirProtocolException {
        IUSBHeader iUSBHeader = new IUSBHeader();
        try {
            iUSBHeader.read(byteBuffer);
            return (int) iUSBHeader.dataPacketLen;
        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    @Override // com.ami.iusb.protocol.RedirProtocol
    public RedirPacket getPacket(ByteBuffer byteBuffer) throws RedirProtocolException {
        IUSBHeader iUSBHeader = new IUSBHeader();
        try {
            iUSBHeader.read(byteBuffer);
            IUSBSCSI iusbscsi = new IUSBSCSI(iUSBHeader);
            iusbscsi.readData(byteBuffer);
            return iusbscsi;
        } catch (BufferUnderflowException e) {
            return null;
        }
    }
}
