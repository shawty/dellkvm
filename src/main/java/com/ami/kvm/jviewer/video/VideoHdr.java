package com.ami.kvm.jviewer.video;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VideoHdr {
    public static final short VIDEOD_HDR_SIZE = 39;
    public static byte[] b1Reserved = new byte[19];
    public static byte[] m_bReserved = {0, 0};
    public static byte[] m_bSignature = {65, 83, 80, 45, 50, 48, 48, 48};
    public static int m_iData_Len = 0;
    public static short m_sCommand = 0;
    public static short m_sHeader_Len = 39;
    public static short m_sStatus = 0;

    public byte[] set(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[(byteBuffer.limit() - 39)];
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.get(b1Reserved);
        byteBuffer.get(m_bSignature);
        m_sHeader_Len = byteBuffer.getShort();
        m_iData_Len = byteBuffer.getInt();
        m_sCommand = byteBuffer.getShort();
        m_sStatus = byteBuffer.getShort();
        byteBuffer.get(m_bReserved);
        byteBuffer.get(bArr);
        return bArr;
    }
}
