package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.hid.KMCrypt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PS2KeyboardRep extends PS2Message {
    private byte[] m_encReport;
    public byte m_keyType;
    public byte[] m_keys;
    private byte[] m_ps2Data;
    private ByteBuffer m_repBuf;
    private byte[] m_report;
    public byte m_rsvd1;

    public PS2KeyboardRep() {
        this((byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }

    public PS2KeyboardRep(byte b, byte b2, byte b3, byte b4) {
        this.m_keys = new byte[3];
        this.m_vHdr = new IVTPPktHdr((byte) 6, 21, 0);
        this.m_devType = 64;
        this.m_protocol = IVTPPktHdr.ADVISER_SHIFT_IMAGE_LEFT;
        this.m_dataLen = 5;
        this.m_keyType = b;
        this.m_keys[0] = b2;
        this.m_keys[1] = b3;
        this.m_keys[2] = b4;
        this.m_rsvd1 = 0;
        this.m_ps2Data = new byte[5];
        this.m_report = new byte[28];
        this.m_encReport = new byte[31];
    }

    public void set(byte b, byte b2, byte b3, byte b4) {
        this.m_keyType = b;
        this.m_keys[0] = b2;
        this.m_keys[1] = b3;
        this.m_keys[2] = b4;
        this.m_rsvd1 = 0;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.PS2Message
    public byte[] report() {
        this.m_repBuf = ByteBuffer.wrap(this.m_report);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        this.m_vHdr.setSize(21);
        this.m_vHdr.setStatus(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_dataLen = 5;
        this.m_rsvd[0] = 0;
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put(this.m_devType);
        this.m_repBuf.put(this.m_protocol);
        this.m_repBuf.putInt(this.m_dataLen);
        this.m_repBuf.put(this.m_rsvd);
        this.m_repBuf.put(this.m_keyType);
        this.m_repBuf.put(this.m_keys);
        this.m_repBuf.put(this.m_rsvd1);
        return this.m_report;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.PS2Message
    public byte[] encryptedReport(KMCrypt kMCrypt) {
        ByteBuffer wrap = ByteBuffer.wrap(this.m_ps2Data);
        wrap.position(0);
        wrap.put(this.m_keyType);
        wrap.put(this.m_keys);
        wrap.put(this.m_rsvd1);
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
