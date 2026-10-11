package com.ami.kvm.jviewer.video;

public class Unsigned4ByteInt {
    int m_uirecvdnum;

    public Unsigned4ByteInt() {
    }

    public Unsigned4ByteInt(int i) {
        this.m_uirecvdnum = i;
    }

    public void setValue(int i) {
        this.m_uirecvdnum = i;
    }

    public int getInt() {
        return this.m_uirecvdnum;
    }

    public long getLong() {
        return ((long) this.m_uirecvdnum) & 4294967295L;
    }

    public long inc() {
        return (long) (this.m_uirecvdnum + 1);
    }
}
