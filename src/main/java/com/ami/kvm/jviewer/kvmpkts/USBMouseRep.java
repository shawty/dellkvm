package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.VideoSettings;
import com.ami.kvm.jviewer.hid.KMCrypt;
import java.awt.Toolkit;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class USBMouseRep extends USBMessage {
    public static byte ABSOLUTE_MOUSE_MODE = 2;
    private static final int DIRABS_MAX_SCALED_X = 32767;
    private static final int DIRABS_MAX_SCALED_Y = 32767;
    public static byte INVALID_MOUSE_MODE = 3;
    public static byte RELATIVE_MOUSE_MODE = 1;
    private static int SeqNum = 0;
    public static byte m_btnStatus = 0;
    private byte m_MouseMode;
    private byte[] m_encReport;
    private ByteBuffer m_repBuf;
    private byte[] m_report;
    private double m_screenheight;
    private double m_screenwidth;
    Toolkit m_toolkit;
    private byte[] m_usbData;
    public int m_xDisp;
    public int m_yDisp;

    public USBMouseRep() {
        this((byte) 0, 0, 0);
    }

    public USBMouseRep(byte b, int i, int i2) {
        this.m_MouseMode = INVALID_MOUSE_MODE;
        this.m_toolkit = Toolkit.getDefaultToolkit();
        m_btnStatus = b;
        this.m_xDisp = i;
        this.m_yDisp = i2;
    }

    public void set(byte b, int i, int i2, byte b2, int i3, int i4) {
        m_btnStatus = b;
        this.m_xDisp = i;
        this.m_yDisp = i2;
        this.m_MouseMode = b2;
        this.m_screenwidth = (double) i3;
        this.m_screenheight = (double) i4;
    }

    public byte[] ABSreport(KMCrypt kMCrypt) {
        int i = 0;
        short s = (short) ((int) ((((double) (this.m_xDisp * 32767)) / this.m_screenwidth) + 0.5d));
        short s2 = (short) ((int) ((((double) (this.m_yDisp * 32767)) / this.m_screenheight) + 0.5d));
        if (kMCrypt != null) {
            this.m_vHdr = new IVTPPktHdr((byte) 6, 41, 0);
            this.m_report = new byte[48];
        } else {
            this.m_vHdr = new IVTPPktHdr((byte) 6, 38, 0);
            this.m_report = new byte[45];
        }
        this.m_repBuf = ByteBuffer.wrap(this.m_report);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        if (kMCrypt != null) {
            this.m_vHdr.setSize(41);
            this.m_vHdr.setStatus(IVTPPktHdr.ENCRYPTION_ENABLED);
        } else {
            this.m_vHdr.setSize(38);
            this.m_vHdr.setStatus(0);
        }
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(6);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 49);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put(Byte.MIN_VALUE);
        this.m_repBuf.put((byte) 2);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(SeqNum);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 5);
        if (kMCrypt != null) {
            byte[] bArr = new byte[5];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            wrap.put(m_btnStatus);
            wrap.putShort(s);
            wrap.putShort(s2);
            byte[] encrypt = kMCrypt.encrypt(bArr);
            if (encrypt == null) {
                return ABSreport(null);
            }
            this.m_repBuf.put(encrypt);
        } else {
            this.m_repBuf.put(m_btnStatus);
            this.m_repBuf.putShort(s);
            this.m_repBuf.putShort(s2);
        }
        for (int i2 = 7; i2 < 39; i2++) {
            i = (i + (this.m_repBuf.get(i2) & 255)) & VideoSettings.GAIN_MAX;
        }
        this.m_repBuf.put(18, (byte) (-((byte) (i & VideoSettings.GAIN_MAX))));
        SeqNum++;
        return this.m_report;
    }

    public byte[] RELreport(KMCrypt kMCrypt) {
        int i = 0;
        if (kMCrypt != null) {
            this.m_vHdr = new IVTPPktHdr((byte) 6, 41, 0);
            this.m_report = new byte[48];
        } else {
            this.m_vHdr = new IVTPPktHdr((byte) 6, 36, 0);
            this.m_report = new byte[43];
        }
        this.m_repBuf = ByteBuffer.wrap(this.m_report);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        if (kMCrypt != null) {
            this.m_vHdr.setSize(41);
            this.m_vHdr.setStatus(IVTPPktHdr.ENCRYPTION_ENABLED);
        } else {
            this.m_vHdr.setSize(36);
            this.m_vHdr.setStatus(0);
        }
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(4);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 49);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put(Byte.MIN_VALUE);
        this.m_repBuf.put((byte) 2);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(SeqNum);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 3);
        if (kMCrypt != null) {
            byte[] bArr = new byte[3];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.put(m_btnStatus);
            wrap.put((byte) this.m_xDisp);
            wrap.put((byte) this.m_yDisp);
            byte[] encrypt = kMCrypt.encrypt(bArr);
            if (encrypt == null) {
                return RELreport(null);
            }
            this.m_repBuf.put(encrypt);
        } else {
            this.m_repBuf.put(m_btnStatus);
            this.m_repBuf.put((byte) this.m_xDisp);
            this.m_repBuf.put((byte) this.m_yDisp);
        }
        for (int i2 = 7; i2 < 39; i2++) {
            i = (i + (this.m_repBuf.get(i2) & 255)) & VideoSettings.GAIN_MAX;
        }
        this.m_repBuf.put(18, (byte) (-((byte) (i & VideoSettings.GAIN_MAX))));
        SeqNum++;
        return this.m_report;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.USBMessage
    public byte[] report() {
        if (this.m_MouseMode == ABSOLUTE_MOUSE_MODE) {
            return ABSreport(null);
        }
        if (this.m_MouseMode == RELATIVE_MOUSE_MODE) {
            return RELreport(null);
        }
        Debug.out.println("INVALID_MOUSE_MODE");
        return null;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.USBMessage
    public byte[] encryptedReport(KMCrypt kMCrypt) {
        if (this.m_MouseMode == ABSOLUTE_MOUSE_MODE) {
            return ABSreport(kMCrypt);
        }
        if (this.m_MouseMode == RELATIVE_MOUSE_MODE) {
            return RELreport(kMCrypt);
        }
        Debug.out.println("INVALID_MOUSE_MODE");
        return null;
    }
}
