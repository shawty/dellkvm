package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.kvmpkts.ASTCursorPktHdr;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ASTCursorHdrReader implements KVMReader {
    private static int SIZE = 57;
    private KVMClient m_client;
    private ASTCursorPktHdr m_cursorHdrcur = null;
    private ASTCursorPktHdr m_cursorHdrprev = null;
    private ByteBuffer m_hdrBuf;
    private int m_ix;
    private int m_pktSize;

    public ASTCursorHdrReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
        this.m_hdrBuf = ByteBuffer.allocate(SIZE);
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_ix = 0;
        this.m_hdrBuf.clear();
        this.m_hdrBuf.position(0);
        this.m_hdrBuf.limit(SIZE);
        this.m_pktSize = i;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            this.m_ix += socketChannel.read(this.m_hdrBuf);
            if (SIZE <= this.m_ix) {
                this.m_cursorHdrcur = new ASTCursorPktHdr();
                this.m_cursorHdrcur.set(this.m_hdrBuf);
                if (this.m_cursorHdrprev == null) {
                    this.m_cursorHdrprev = new ASTCursorPktHdr();
                    this.m_cursorHdrprev = this.m_cursorHdrcur;
                }
                if (this.m_cursorHdrcur.checksum == 0) {
                    this.m_cursorHdrprev.xpos = this.m_cursorHdrcur.xpos;
                    this.m_cursorHdrprev.ypos = this.m_cursorHdrcur.ypos;
                } else {
                    this.m_cursorHdrprev = this.m_cursorHdrcur;
                }
                MouseReader mouseReader = this.m_client.getMouseReader(this.m_cursorHdrprev);
                mouseReader.initialize(this.m_pktSize - SIZE);
                this.m_client.setState(mouseReader);
            }
            return 0;
        } catch (Exception e) {
            Debug.out.println("MOUSE_RDR " + e);
            return -1;
        }
    }
}
