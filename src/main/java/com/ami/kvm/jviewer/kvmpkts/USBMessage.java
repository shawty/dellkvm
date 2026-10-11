package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.hid.KMCrypt;

public abstract class USBMessage {
    public static final byte IUSB_DEVICE_KEYBD = 48;
    public static final byte IUSB_DEVICE_MOUSE = 49;
    public static final int IUSB_FROM_REMOTE = 128;
    public static final byte IUSB_HDR_SIZE = 32;
    public static final int IUSB_HID_HDR_SIZE = 34;
    public static final byte IUSB_KEYBD_DEVNUM = 2;
    public static final byte IUSB_KEYBD_IFNUM = 0;
    public static final byte IUSB_MAJOR_NUM = 1;
    public static final byte IUSB_MINOR_NUM = 0;
    public static final byte IUSB_MOUSE_DEVNUM = 2;
    public static final byte IUSB_MOUSE_IFNUM = 1;
    public static final byte IUSB_PROTO_KEYBD_DATA = 16;
    public static final byte IUSB_PROTO_MOUSE_DATA = 32;
    public static final int IUSB_TO_REMOTE = 0;
    public static final int USB_PKT_ENC_KEYBDREP_SIZE = 8;
    public static final int USB_PKT_ENC_MOUSEREP_SIZE = 8;
    public static final int USB_PKT_KEYBDREP_SIZE = 8;
    public static final int USB_PKT_MOUSE_ABS_REP_SIZE = 5;
    public static final int USB_PKT_MOUSE_REL_REP_SIZE = 3;
    public int m_dataLen;
    public byte m_devType;
    public byte m_protocol;
    public byte[] m_rsvd = new byte[2];
    public byte[] m_signature = {73, 85, 83, 66, 32, 32, 32, 32};
    public IVTPPktHdr m_vHdr;

    public abstract byte[] encryptedReport(KMCrypt kMCrypt);

    public abstract byte[] report();
}
