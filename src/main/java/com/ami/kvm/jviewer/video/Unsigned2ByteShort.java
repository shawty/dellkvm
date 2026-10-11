package com.ami.kvm.jviewer.video;

public class Unsigned2ByteShort {
    short m_uirecvdnum;

    public Unsigned2ByteShort() {
    }

    public Unsigned2ByteShort(short s) {
        this.m_uirecvdnum = s;
    }

    public void setValue(short s) {
        this.m_uirecvdnum = s;
    }

    public short getShort() {
        return this.m_uirecvdnum;
    }

    public int getInt() {
        return this.m_uirecvdnum & 65535;
    }
}
