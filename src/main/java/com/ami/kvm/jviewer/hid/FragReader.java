package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class FragReader implements KVMReader {
    private KVMClient m_client;
    private int m_fragIx;
    private byte[] m_frameBuf = new byte[KVMClient.MAX_FRAGMENT_SIZE];
    private ByteBuffer m_frameByteBuf = ByteBuffer.wrap(this.m_frameBuf);
    private int m_frameIx = 0;

    public FragReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        if ((this.m_client.m_fragNum & Short.MAX_VALUE) == 0) {
            this.m_frameIx = 0;
        }
        this.m_frameByteBuf.position(this.m_frameIx);
        this.m_frameByteBuf.limit((this.m_frameIx + this.m_client.m_pktHdr.pktSize) - 2);
        this.m_fragIx = 0;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            this.m_fragIx += socketChannel.read(this.m_frameByteBuf);
            if (this.m_fragIx != this.m_client.m_pktHdr.pktSize - 2) {
                return 0;
            }
            this.m_frameIx += this.m_fragIx;
            if ((this.m_client.m_fragNum & 32768) != 0) {
                this.m_frameByteBuf.limit(this.m_frameIx);
                if (this.m_frameByteBuf == null) {
                }
                this.m_client.onNewFrame(this.m_frameByteBuf);
                this.m_frameIx = 0;
            }
            HeaderReader hdrReader = this.m_client.getHdrReader();
            hdrReader.initialize(0);
            this.m_client.setState(hdrReader);
            return 0;
        } catch (Exception e) {
            Debug.out.println("FRAG_RDR " + e);
            return -1;
        }
    }
}
