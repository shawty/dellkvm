package com.ami.kvm.jviewer.communication;

import com.ami.kvm.jviewer.Debug;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class Client {
    public static final int RETRY_COUNT = 3;
    public static final int TIMEOUT = 1;
    protected byte[] m_ipAddr;
    protected int m_port;
    protected SocketChannel m_sockCh;

    public abstract void onReadEvent();

    public Client(byte[] bArr, int i) {
        this.m_ipAddr = bArr;
        this.m_port = i;
    }

    public SocketChannel getSockChannel() {
        return this.m_sockCh;
    }

    public int connect() throws IOException {
        this.m_sockCh = SocketChannel.open();
        this.m_sockCh.configureBlocking(false);
        this.m_sockCh.connect(new InetSocketAddress(InetAddress.getByAddress(this.m_ipAddr), this.m_port));
        int i = 3;
        while (!this.m_sockCh.finishConnect()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            int i2 = i - 1;
            if (i <= 0) {
                return -1;
            }
            i = i2;
        }
        return 0;
    }

    public int sendMessage(byte[] bArr, int i) {
        try {
            return this.m_sockCh.write(ByteBuffer.wrap(bArr));
        } catch (IOException e) {
            Debug.out.println("Send message failed");
            return 0;
        }
    }

    public void close() {
        try {
            this.m_sockCh.close();
        } catch (IOException e) {
        }
    }
}
