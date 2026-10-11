package com.ami.kvm.jviewer.video;

import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import java.nio.ByteBuffer;

public class ColorGains {
    public static final int DEFAULT_COLOR_GAIN = 85;
    public int blueCurrent;
    public int blueDefault;
    public int greenCurrent;
    public int greenDefault;
    public int redCurrent;
    public int redDefault;

    public ColorGains(ByteBuffer byteBuffer) {
        this.redCurrent = intOf(byteBuffer.get(0));
        this.greenCurrent = intOf(byteBuffer.get(1));
        this.blueCurrent = intOf(byteBuffer.get(2));
        this.redDefault = intOf(byteBuffer.get(3));
        this.greenDefault = intOf(byteBuffer.get(4));
        this.blueDefault = intOf(byteBuffer.get(5));
    }

    private int intOf(byte b) {
        return (b & 128) != 0 ? (b & Byte.MAX_VALUE) + USBMessage.IUSB_FROM_REMOTE : b & Byte.MAX_VALUE;
    }
}
