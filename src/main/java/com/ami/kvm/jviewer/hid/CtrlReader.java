package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CtrlReader implements KVMReader {
    private static final int CTRL_MSG_BODY_SIZE = 128;
    private KVMClient m_client;
    private ByteBuffer m_ctrlBuf = ByteBuffer.allocate(128);
    private int m_ctrlIx = 0;

    public CtrlReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_ctrlIx = 0;
        this.m_ctrlBuf.clear();
        this.m_ctrlBuf.limit(this.m_client.m_pktHdr.pktSize);
        this.m_ctrlBuf.position(0);
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        if (this.m_client.m_pktHdr.pktSize <= 0) {
            return -1;
        }
        try {
            this.m_ctrlIx += socketChannel.read(this.m_ctrlBuf);
            if (this.m_client.m_pktHdr.pktSize <= this.m_ctrlIx) {
                this.m_client.m_ctrlMsg = this.m_ctrlBuf;
                this.m_client.onControlMessage();
                HeaderReader hdrReader = this.m_client.getHdrReader();
                hdrReader.initialize(0);
                this.m_client.setState(hdrReader);
            }
            return 0;
        } catch (Exception e) {
            Debug.out.println("CTRL_RDR " + e);
            return -1;
        }
    }
}
