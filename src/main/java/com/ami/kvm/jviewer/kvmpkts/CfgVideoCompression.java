package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CfgVideoCompression {
    public static final int CFG_VIDEO_COMPRESSION_SIZE = 1;
    public byte m_compType;
    ByteBuffer m_repBuf = ByteBuffer.wrap(this.m_report);
    private byte[] m_report = new byte[8];
    public IVTPPktHdr m_vHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_SET_COMPRESSION_TYPE, 1, 0);

    public CfgVideoCompression(byte b) {
        this.m_compType = b;
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.limit(this.m_report.length);
    }

    public int size() {
        return 8;
    }

    public byte[] report() {
        this.m_repBuf.position(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_compType);
        return this.m_report;
    }
}
