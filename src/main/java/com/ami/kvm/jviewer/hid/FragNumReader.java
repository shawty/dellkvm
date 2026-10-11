package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class FragNumReader implements KVMReader {
    private KVMClient m_client;
    private ByteBuffer m_fragNumBuf = ByteBuffer.allocate(2);
    private int m_fragNumIx = 0;

    public FragNumReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_fragNumIx = 0;
        this.m_fragNumBuf.clear();
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            this.m_fragNumIx += socketChannel.read(this.m_fragNumBuf);
            if (2 > this.m_fragNumIx) {
                return 0;
            }
            this.m_fragNumBuf.rewind();
            this.m_fragNumBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.m_client.m_fragNum = this.m_fragNumBuf.getShort(0);
            FragReader fragReader = this.m_client.getFragReader();
            fragReader.initialize(0);
            this.m_client.setState(fragReader);
            return 0;
        } catch (Exception e) {
            Debug.out.println("ON_READ_FRAG_SZ " + e);
            return -1;
        }
    }
}
