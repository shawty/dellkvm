package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CfgQualityLvl {
    public static final int CFG_QUALITY_LVL_SIZE = 1;
    public static final byte QUALITY_1_BEST_SPEED = 35;
    public static final byte QUALITY_2 = 25;
    public static final byte QUALITY_3 = 20;
    public static final byte QUALITY_4 = 15;
    public static final byte QUALITY_5_BEST_QUALITY = 10;
    public byte m_quality;
    ByteBuffer m_repBuf = ByteBuffer.wrap(this.m_report);
    private byte[] m_report = new byte[8];
    public IVTPPktHdr m_vHdr = new IVTPPktHdr((byte) 10, 1, 0);

    public CfgQualityLvl(byte b) {
        this.m_quality = b;
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.limit(this.m_report.length);
    }

    public int size() {
        return 8;
    }

    public byte[] report() {
        this.m_repBuf.position(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_quality);
        return this.m_report;
    }
}
