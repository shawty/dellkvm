package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HeaderReader implements KVMReader {
    private KVMClient m_client;
    private byte[] m_hdrBuf = new byte[7];
    private ByteBuffer m_hdrByteBuf = ByteBuffer.wrap(this.m_hdrBuf);
    private int m_hdrIx = 0;
    private IVTPPktHdr m_pktHdr = new IVTPPktHdr();

    public HeaderReader(KVMClient kVMClient) {
        this.m_client = kVMClient;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_hdrIx = 0;
        this.m_hdrByteBuf.clear();
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            this.m_hdrIx += socketChannel.read(this.m_hdrByteBuf);
            if (7 > this.m_hdrIx) {
                return 0;
            }
            this.m_pktHdr.set(this.m_hdrByteBuf);
            this.m_client.m_pktHdr = this.m_pktHdr;
            if (this.m_pktHdr.type == 5) {
                FragNumReader fragNumReader = this.m_client.getFragNumReader();
                fragNumReader.initialize(0);
                this.m_client.setState(fragNumReader);
                return 0;
            } else if (this.m_pktHdr.type == 32) {
                NullReader nullReader = this.m_client.getNullReader();
                nullReader.initialize(0);
                this.m_client.setState(nullReader);
                return 0;
            } else if (this.m_pktHdr.type == 48) {
                ASTCursorHdrReader cursorReader = this.m_client.getCursorReader();
                cursorReader.initialize(this.m_pktHdr.pktSize);
                this.m_client.setState(cursorReader);
                return 0;
            } else if (this.m_pktHdr.pktSize > 0) {
                CtrlReader ctrlReader = this.m_client.getCtrlReader();
                ctrlReader.initialize(0);
                this.m_client.setState(ctrlReader);
                return 0;
            } else {
                this.m_client.onControlMessage();
                HeaderReader hdrReader = this.m_client.getHdrReader();
                hdrReader.initialize(0);
                this.m_client.setState(hdrReader);
                return 0;
            }
        } catch (Exception e) {
            Debug.out.println("HDR_RDR " + e);
            return -1;
        }
    }
}
