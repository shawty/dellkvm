package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CfgBandwidth {
    public static final int BANDWIDTH_100MBPS = 13107200;
    public static final int BANDWIDTH_10MBPS = 1310720;
    public static final int BANDWIDTH_1MBPS = 131072;
    public static final int BANDWIDTH_256KBPS = 32768;
    public static final int BANDWIDTH_512KBPS = 65536;
    public static final int CFG_BANDWIDTH_SIZE = 4;
    public int m_bandwidth;
    ByteBuffer m_repBuf = ByteBuffer.wrap(this.m_report);
    private byte[] m_report = new byte[11];
    public IVTPPktHdr m_vHdr = new IVTPPktHdr((byte) 7, 4, 0);

    public CfgBandwidth(int i) {
        this.m_bandwidth = i;
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.limit(this.m_report.length);
    }

    public int size() {
        return 11;
    }

    public byte[] report() {
        this.m_repBuf.position(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.putInt(this.m_bandwidth);
        return this.m_report;
    }

    public static int determineBandwidth(long j) {
        double d = 1.31072E7d / (((double) j) / 1000.0d);
        Debug.out.println("bps " + d);
        if (d < 49152.0d) {
            return BANDWIDTH_256KBPS;
        }
        if (d < 98304.0d) {
            return BANDWIDTH_512KBPS;
        }
        if (d < 720896.0d) {
            return BANDWIDTH_1MBPS;
        }
        if (d < 7208960.0d) {
            return BANDWIDTH_10MBPS;
        }
        return BANDWIDTH_100MBPS;
    }
}
