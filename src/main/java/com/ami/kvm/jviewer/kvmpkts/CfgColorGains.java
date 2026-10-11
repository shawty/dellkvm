package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CfgColorGains {
    public static final int CFG_COLOR_GAINS_SIZE = 6;
    public static byte sBlueDef = 85;
    public static byte sGreenDef = 85;
    public static byte sRedDef = 85;
    public byte m_blueGain;
    public byte m_greenGain;
    public byte m_redGain;
    ByteBuffer m_repBuf = ByteBuffer.wrap(this.m_report);
    private byte[] m_report = new byte[13];
    public IVTPPktHdr m_vHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_ADJUST_COLOR_GAIN, 6, 0);

    public CfgColorGains(byte b, byte b2, byte b3) {
        this.m_redGain = b;
        this.m_greenGain = b2;
        this.m_blueGain = b3;
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.limit(this.m_report.length);
    }

    public int size() {
        return 13;
    }

    public byte[] report() {
        this.m_repBuf.position(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_redGain);
        this.m_repBuf.put(this.m_greenGain);
        this.m_repBuf.put(this.m_blueGain);
        this.m_repBuf.put(sRedDef);
        this.m_repBuf.put(sGreenDef);
        this.m_repBuf.put(sBlueDef);
        return this.m_report;
    }
}
