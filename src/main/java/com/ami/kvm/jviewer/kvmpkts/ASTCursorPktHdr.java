package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ASTCursorPktHdr {
    public static final int HDR_SIZE = 57;
    public int checksum;
    public int cursorType;
    public byte[] reserved = new byte[41];
    public short xoffset;
    public short xpos;
    public short yoffset;
    public short ypos;

    public void set(ByteBuffer byteBuffer) {
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.get(this.reserved);
        this.cursorType = byteBuffer.getInt();
        this.checksum = byteBuffer.getInt();
        this.xpos = byteBuffer.getShort();
        this.ypos = byteBuffer.getShort();
        this.xoffset = byteBuffer.getShort();
        this.yoffset = byteBuffer.getShort();
    }
}
