package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NullReader implements KVMReader {
    private KVMClient m_client;
    private ByteBuffer m_nullBuf = ByteBuffer.allocate(262144);
    private int m_nullIx;
    private int m_numRead;
    private int m_readCnt;
    private int m_readSz;

    public NullReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_readSz = this.m_client.m_pktHdr.pktSize;
        this.m_numRead = this.m_client.m_pktHdr.status;
        this.m_nullIx = 0;
        this.m_readCnt = 0;
        this.m_nullBuf.position(0);
        this.m_nullBuf.limit(this.m_readSz);
        this.m_client.onStartReading();
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            this.m_nullIx += socketChannel.read(this.m_nullBuf);
            if (this.m_readSz > this.m_nullIx) {
                return 0;
            }
            this.m_readCnt++;
            if (this.m_numRead <= this.m_readCnt) {
                this.m_client.onStopReading();
                HeaderReader hdrReader = this.m_client.getHdrReader();
                hdrReader.initialize(0);
                this.m_client.setState(hdrReader);
                return 0;
            }
            this.m_nullIx = 0;
            this.m_nullBuf.position(0);
            return 0;
        } catch (Exception e) {
            Debug.out.println("CTRL_RDR " + e);
            return -1;
        }
    }
}
