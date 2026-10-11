package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.hid.KMCrypt;

public abstract class PS2Message {
    public static final int IPS2_DEVICE_KEYBD = 64;
    public static final int IPS2_DEVICE_MOUSE = 65;
    public static final int IPS2_PROTO_KEYBD_DATA = 18;
    public static final int IPS2_PROTO_MOUSE_DATA = 34;
    public static final int PS2_PKT_ENC_KEYBDREP_SIZE = 8;
    public static final int PS2_PKT_ENC_MOUSEREP_SIZE = 8;
    public static final int PS2_PKT_HDR_SIZE = 16;
    public static final int PS2_PKT_KEYBDREP_SIZE = 5;
    public static final int PS2_PKT_MOUSEREP_SIZE = 3;
    public int m_dataLen;
    public byte m_devType;
    public byte m_protocol;
    public byte[] m_rsvd = new byte[2];
    public byte[] m_signature = {73, 80, 83, 50, 32, 32, 32, 32};
    public IVTPPktHdr m_vHdr;

    public abstract byte[] encryptedReport(KMCrypt kMCrypt);

    public abstract byte[] report();
}
