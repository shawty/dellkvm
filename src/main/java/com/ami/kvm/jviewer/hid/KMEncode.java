package com.ami.kvm.jviewer.hid;

import java.awt.event.KeyEvent;

public abstract class KMEncode {
    public abstract int encodeKey(KeyEvent keyEvent, byte[] bArr);
}
