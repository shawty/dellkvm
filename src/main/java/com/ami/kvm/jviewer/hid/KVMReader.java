package com.ami.kvm.jviewer.hid;

import java.nio.channels.SocketChannel;

public interface KVMReader {
    void initialize(int i);

    int read(SocketChannel socketChannel);
}
