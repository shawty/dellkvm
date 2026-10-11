package com.ami.kvm.jviewer.kvmpkts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IVTPPktHdr {
    public static final byte ADVISER_ADJUST_COLOR_GAIN = 11;
    public static final byte ADVISER_AST2K_ADD_NEW_MOUSE_CURSOR = 48;
    public static final byte ADVISER_AST2K_GET_VIDEO_ENGINE_CONFIGS = 41;
    public static final byte ADVISER_AST2K_SET_VIDEO_ENGINE_CONFIGS = 40;
    public static final byte ADVISER_AUTO_CALIBRATE = 22;
    public static final byte ADVISER_BLANK_SCREEN = 15;
    public static final byte ADVISER_BW_DETECT_REQ = 31;
    public static final byte ADVISER_BW_DETECT_RES = 32;
    public static final int ADVISER_CLIENT = 1;
    public static final byte ADVISER_DEFAULT_COLOR_GAIN = 17;
    public static final byte ADVISER_DISABLE_ENCRYPTION = 27;
    public static final byte ADVISER_ENABLE_ENCRYPTION = 26;
    public static final byte ADVISER_ENCRYPTION_KEY = 28;
    public static final byte ADVISER_ENCRYPTION_STATUS = 29;
    public static final byte ADVISER_GET_CHALLENGE = 1;
    public static final byte ADVISER_GET_COLOR_GAIN = 16;
    public static final byte ADVISER_GET_COLOR_GAIN_RESPONSE = 23;
    public static final byte ADVISER_GET_USB_MOUSE_MODE = 33;
    public static final byte ADVISER_HID_PKT = 6;
    public static final byte ADVISER_INITIAL_ENCRYPTION_STATUS = 30;
    public static final byte ADVISER_INVALIDCMD = 3;
    public static final byte ADVISER_LOGIN = 2;
    public static final byte ADVISER_MAX_NUM_SESSION = 49;
    public static final byte ADVISER_PAUSE_REDIRECTION = 13;
    public static final byte ADVISER_REFRESH_VIDEO_SCREEN = 12;
    public static final byte ADVISER_RESUME_REDIRECTION = 14;
    public static final int ADVISER_SERVER = 0;
    public static final byte ADVISER_SET_BANDWIDTH = 7;
    public static final byte ADVISER_SET_COMPRESSION_TYPE = 24;
    public static final byte ADVISER_SET_CUSTOM_QUALITY = 9;
    public static final byte ADVISER_SET_FPS = 10;
    public static final byte ADVISER_SET_QUALITY_LEVEL = 8;
    public static final byte ADVISER_SHIFT_IMAGE_DOWN = 21;
    public static final byte ADVISER_SHIFT_IMAGE_LEFT = 18;
    public static final byte ADVISER_SHIFT_IMAGE_RIGHT = 19;
    public static final byte ADVISER_SHIFT_IMAGE_UP = 20;
    public static final byte ADVISER_STOP_SESSION_IMMEDIATE = 25;
    public static final byte ADVISER_VALIDATE_CDROM_SESSION = 36;
    public static final byte ADVISER_VALIDATE_CDROM_SESSION_RESPONSE = 37;
    public static final byte ADVISER_VALIDATE_FLOPPY_SESSION = 38;
    public static final byte ADVISER_VALIDATE_FLOPPY_SESSION_RESPONSE = 39;
    public static final byte ADVISER_VALIDATE_VIDEO_SESSION = 34;
    public static final byte ADVISER_VALIDATE_VIDEO_SESSION_RESPONSE = 35;
    public static final byte ADVISER_VIDEO_FRAGMENT = 5;
    public static final short CAPTURE_CLEAR_BUFFER = 2;
    public static final short CAPTURE_EVEN_FRAME = 0;
    public static final short CAPTURE_FRAME_BIT = 1;
    public static final short CAPTURE_ODD_FRAME = 1;
    public static boolean CONVERT_TO_RGB = false;
    public static final byte DUMMY_LOGIN = 4;
    public static final short ENCRYPTION_ENABLED = 255;
    public static final double GAMMA1PARAMETER = 1.0d;
    public static final double GAMMA1_GAMMA2_SEPERATE = 128.0d;
    public static final double GAMMA2PARAMETER = 1.0d;
    public static final byte HASH_SIZE = 16;
    public static final int HDR_SIZE = 7;
    public static final short INIT_QLZW = 4;
    public static final int MAX_AVICA_PARAMS = 8;
    public static final int MAX_CHALLENGE_LEN = 32;
    public static final int MAX_USERNAME_LEN = 32;
    public static boolean ORIGINAL = false;
    public static final byte SESSION_TOKEN_LEN = 16;
    public static final int STD_DIGEST_LEN = 16;
    public static int VIRTADD = 0;
    public static boolean WRITE_COMPRESSED_DATA_TO_FILE = false;
    public static int gCheck;
    public static int gTxb;
    public static int gTyb;
    public static long gpixel;
    private ByteBuffer m_buf;
    public int pktSize;
    public short status;
    public byte type;

    public IVTPPktHdr() {
        this((byte) 0, 0, 0);
    }

    public IVTPPktHdr(byte b, int i, short s) {
        this.type = b;
        this.pktSize = i;
        this.status = s;
        this.m_buf = ByteBuffer.wrap(new byte[7]);
        this.m_buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public final void set(ByteBuffer byteBuffer) {
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.type = byteBuffer.get();
        this.pktSize = byteBuffer.getInt();
        this.status = byteBuffer.getShort();
    }

    /* access modifiers changed from: package-private */
    public final void setSize(int i) {
        this.pktSize = i;
    }

    /* access modifiers changed from: package-private */
    public final void setStatus(short s) {
        this.status = s;
    }

    public final byte type() {
        return this.type;
    }

    public final int pktSize() {
        return this.pktSize;
    }

    public final short status() {
        return this.status;
    }

    public final int size() {
        return 7;
    }

    public final byte[] array() {
        this.m_buf.position(0);
        this.m_buf.put(this.type);
        this.m_buf.putInt(this.pktSize);
        this.m_buf.putShort(this.status);
        return this.m_buf.array();
    }
}
