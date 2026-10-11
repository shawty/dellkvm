package com.ami.kvm.jviewer.kvmpkts;

public interface KeyProcessor {
    byte[] convertKeyCode(int i, int i2, boolean z);

    boolean getAutoKeybreakMode();

    void setAutoKeybreakMode(boolean z);
}
