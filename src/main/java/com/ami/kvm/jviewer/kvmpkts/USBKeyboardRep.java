package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.gui.VideoSettings;
import com.ami.kvm.jviewer.hid.KMCrypt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class USBKeyboardRep extends USBMessage {
    static int SeqNum = 0;
    private int keyCode;
    private int keyLocation;
    private boolean keyPress;
    private USBKeyProcessor m_USBKeyProcessor;
    private byte[] m_encReport;
    private ByteBuffer m_repBuf;
    private byte[] m_report;
    private byte[] m_usbHdr;

    public USBKeyboardRep() {
        this(0, 0, true);
    }

    public USBKeyboardRep(int i, int i2, boolean z) {
        this.m_vHdr = new IVTPPktHdr((byte) 6, 41, 0);
        this.m_devType = 48;
        this.m_protocol = 16;
        this.m_dataLen = 8;
        this.m_usbHdr = new byte[33];
        this.m_report = new byte[48];
        this.m_encReport = new byte[48];
        this.m_USBKeyProcessor = new USBKeyProcessor();
    }

    public void set(int i, int i2, boolean z) {
        this.keyCode = i;
        this.keyLocation = i2;
        this.keyPress = z;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.USBMessage
    public byte[] report() {
        int i = 0;
        this.m_repBuf = ByteBuffer.wrap(this.m_report);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        this.m_vHdr.setSize(41);
        this.m_vHdr.setStatus(0);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(9);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 48);
        this.m_repBuf.put((byte) 16);
        this.m_repBuf.put(Byte.MIN_VALUE);
        this.m_repBuf.put((byte) 2);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(SeqNum);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 8);
        byte[] convertKeyCode = this.m_USBKeyProcessor.convertKeyCode(this.keyCode, this.keyLocation, this.keyPress);
        if (convertKeyCode == null) {
            this.m_repBuf.put(new byte[6]);
        } else {
            this.m_repBuf.put(convertKeyCode);
        }
        for (int i2 = 7; i2 < 39; i2++) {
            i = (i + (this.m_repBuf.get(i2) & 255)) & VideoSettings.GAIN_MAX;
        }
        this.m_repBuf.put(18, (byte) (-((byte) (i & VideoSettings.GAIN_MAX))));
        SeqNum++;
        return this.m_report;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.USBMessage
    public byte[] encryptedReport(KMCrypt kMCrypt) {
        int i = 0;
        this.m_repBuf = ByteBuffer.wrap(this.m_encReport);
        this.m_repBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.m_repBuf.position(0);
        this.m_vHdr.setSize(41);
        this.m_vHdr.setStatus(IVTPPktHdr.ENCRYPTION_ENABLED);
        this.m_repBuf.put(this.m_vHdr.array());
        this.m_repBuf.put(this.m_signature);
        this.m_repBuf.put((byte) 1);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 32);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(9);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 48);
        this.m_repBuf.put((byte) 16);
        this.m_repBuf.put(Byte.MIN_VALUE);
        this.m_repBuf.put((byte) 2);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.putInt(SeqNum);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 0);
        this.m_repBuf.put((byte) 8);
        byte[] convertKeyCode = this.m_USBKeyProcessor.convertKeyCode(this.keyCode, this.keyLocation, this.keyPress);
        if (convertKeyCode == null) {
            return report();
        }
        byte[] encrypt = kMCrypt.encrypt(convertKeyCode);
        if (encrypt == null) {
            return report();
        }
        this.m_repBuf.put(encrypt);
        for (int i2 = 7; i2 < 39; i2++) {
            i = (i + (this.m_repBuf.get(i2) & 255)) & VideoSettings.GAIN_MAX;
        }
        this.m_repBuf.put(18, (byte) (-((byte) (i & VideoSettings.GAIN_MAX))));
        SeqNum++;
        return this.m_encReport;
    }

    public void setAutoKeybreakMode(boolean z) {
        this.m_USBKeyProcessor.setAutoKeybreakMode(z);
    }

    public boolean getAutoKeybreakMode() {
        return this.m_USBKeyProcessor.getAutoKeybreakMode();
    }
}
