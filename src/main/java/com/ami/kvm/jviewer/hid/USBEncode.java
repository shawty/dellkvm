package com.ami.kvm.jviewer.hid;

import java.awt.event.KeyEvent;

public class USBEncode extends KMEncode {
    @Override // com.ami.kvm.jviewer.hid.KMEncode
    public int encodeKey(KeyEvent keyEvent, byte[] bArr) {
        return 0;
    }
}
