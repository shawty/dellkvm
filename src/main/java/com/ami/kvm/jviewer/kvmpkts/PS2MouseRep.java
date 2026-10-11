package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.hid.KMCrypt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PS2MouseRep extends PS2Message {
    public byte m_btnStatus;
    private byte[] m_encReport;
    private byte[] m_ps2Data;
    private ByteBuffer m_repBuf;
    private byte[] m_report;
    public byte m_xDisp;
    public byte m_yDisp;

    public PS2MouseRep() {
        this((byte) 0, (byte) 0, (byte) 0);
    }

    public PS2MouseRep(byte b, byte b2, byte b3) {
        this.m_vHdr = new IVTPPktHdr((byte) 6, 19, 0);
        this.m_devType = 65;
        this.m_protocol = IVTPPktHdr.ADVISER_VALIDATE_VIDEO_SESSION;
        this.m_dataLen = 3;
        this.m_btnStatus = b;
        this.m_xDisp = b2;
        this.m_yDisp = b3;
        this.m_ps2Data = new byte[3];
        this.m_report = new byte[26];
        this.m_encReport = new byte[31];
    }

    public void set(byte b, byte b2, byte b3) {
        this.m_btnStatus = b;
        this.m_xDisp = b2;
        this.m_yDisp = b3;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.PS2Message
    public byte[] report() {
        this.m_repBuf = ByteBuffer.wrap(this.m_report);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        this.m_vHdr.setSize(19);
        this.m_vHdr.setStatus(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_dataLen = 3;
        this.m_rsvd[0] = 0;
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put(this.m_devType);
        this.m_repBuf.put(this.m_protocol);
        this.m_repBuf.putInt(this.m_dataLen);
        this.m_repBuf.put(this.m_rsvd);
        this.m_repBuf.put(this.m_btnStatus);
        this.m_repBuf.put(this.m_xDisp);
        this.m_repBuf.put(this.m_yDisp);
        return this.m_report;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.PS2Message
    public byte[] encryptedReport(KMCrypt kMCrypt) {
        ByteBuffer wrap = ByteBuffer.wrap(this.m_ps2Data);
        wrap.position(0);
        wrap.put(this.m_btnStatus);
        wrap.put(this.m_xDisp);
        wrap.put(this.m_yDisp);
        byte[] encrypt = kMCrypt.encrypt(this.m_ps2Data);
        this.m_repBuf = ByteBuffer.wrap(this.m_encReport);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        this.m_vHdr.setSize(24);
        this.m_vHdr.setStatus(IVTPPktHdr.ENCRYPTION_ENABLED);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_dataLen = 8;
        this.m_rsvd[0] = 1;
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put(this.m_devType);
        this.m_repBuf.put(this.m_protocol);
        this.m_repBuf.putInt(this.m_dataLen);
        this.m_repBuf.put(this.m_rsvd);
        this.m_repBuf.put(encrypt);
        return this.m_encReport;
    }
}
