package com.ami.kvm.jviewer;

import com.ami.kvm.jviewer.gui.VideoSettings;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;

public class MiscUtils {
    public static final boolean is_G4 = true;

    public static int ubyte(byte b) {
        int i;
        if ((b & 128) != 0) {
            i = USBMessage.IUSB_FROM_REMOTE;
        } else {
            i = 0;
        }
        return i + (b & Byte.MAX_VALUE);
    }

    public static int ushort(short s) {
        int i = CfgBandwidth.BANDWIDTH_256KBPS;
        if ((s & 32768) == 0) {
            i = 0;
        }
        return i + (s & Short.MAX_VALUE);
    }

    public static byte CalculateModulo100(byte[] bArr) {
        int i = 0;
        for (byte b : bArr) {
            i = ((b & 255) + i) & VideoSettings.GAIN_MAX;
        }
        return (byte) (-((byte) (i & VideoSettings.GAIN_MAX)));
    }
}
