package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AutoDetectBw {
    public static final int AUTO_DETECT_BW_SIZE = 4;
    public int m_load;
    ByteBuffer m_repBuf = ByteBuffer.wrap(this.m_report);
    private byte[] m_report = new byte[11];
    public IVTPPktHdr m_vHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_BW_DETECT_REQ, 4, 0);

    public AutoDetectBw(int i) {
        this.m_load = i;
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.limit(this.m_report.length);
    }

    public int size() {
        return 11;
    }

    public byte[] report() {
        this.m_repBuf.position(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.putInt(this.m_load);
        return this.m_report;
    }
}
