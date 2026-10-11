package com.ami.kvm.jviewer.hid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ASTCursorPatternPkt {
    public static final int ASTCURPAT_PKT_SIZE = 8249;
    int CheckSum;
    int CursorType;
    short[] Pattern = new short[4096];
    short XOffset;
    short XPos;
    short YOffset;
    short YPos;
    byte[] bReserved = new byte[41];

    public void setASTCursorPattern(ByteBuffer byteBuffer) {
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.get(this.bReserved);
        this.CursorType = byteBuffer.getInt();
        this.CheckSum = byteBuffer.getInt();
        this.XPos = byteBuffer.getShort();
        this.YPos = byteBuffer.getShort();
        this.XOffset = byteBuffer.getShort();
        this.YOffset = byteBuffer.getShort();
        for (int i = 0; i < this.Pattern.length; i++) {
            this.Pattern[i] = byteBuffer.getShort();
        }
    }
}
