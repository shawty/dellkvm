package com.ami.kvm.jviewer.kvmpkts;

public class CaptureParams {
    public byte cellX = 0;
    public byte cellY = 0;
    public byte tHigh = 0;
    public byte tLow = 0;
    public byte tScr = 0;
    public byte tStatic = 0;

    public void set(byte b, byte b2, byte b3, byte b4, byte b5, byte b6) {
        this.cellX = b;
        this.cellY = b2;
        this.tLow = b3;
        this.tHigh = b4;
        this.tScr = b5;
        this.tStatic = b6;
    }

    public byte getCellX() {
        return this.cellX;
    }

    public byte getCellY() {
        return this.cellY;
    }

    public byte getTLow() {
        return this.tLow;
    }

    public byte getTHigh() {
        return this.tHigh;
    }

    public byte getTScr() {
        return this.tScr;
    }

    public byte getTStatic() {
        return this.tStatic;
    }
}
