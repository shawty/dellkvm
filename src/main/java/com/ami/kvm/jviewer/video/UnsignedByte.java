package com.ami.kvm.jviewer.video;

public class UnsignedByte {
    byte m_uirecvdnum;

    public UnsignedByte() {
    }

    public UnsignedByte(byte b) {
        this.m_uirecvdnum = b;
    }

    public void setValue(byte b) {
        this.m_uirecvdnum = b;
    }

    public byte getByte() {
        return this.m_uirecvdnum;
    }

    public short getShort() {
        return (short) (this.m_uirecvdnum & 255);
    }
}
