package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.JViewerView;
import com.ami.kvm.jviewer.kvmpkts.ASTCursorPktHdr;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.video.JVVideo;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class MouseReader implements KVMReader {
    private static int SIZE = 8192;
    public static boolean prevremotecurpkt = false;
    private byte ANDBit;
    private int Alpha;
    private byte[] CursorColorData = new byte[3];
    private int CursorData;
    private byte XORBit;
    private KVMClient m_client;
    private Rectangle m_currCursorRect;
    private ASTCursorPktHdr m_cursorHdr;
    private boolean m_firstTime = true;
    private int m_ix;
    private ByteBuffer m_mouseBuf;
    byte[] m_pImageBuffer;
    private short[] m_pattern = new short[(SIZE / 2)];
    private int m_pktSize;
    private Rectangle m_prevCursorRect;
    private JVVideo m_vidClnt;
    private JViewerView m_view;

    public MouseReader(KVMClient kVMClient, JVVideo jVVideo) {
        this.m_client = kVMClient;
        this.m_mouseBuf = ByteBuffer.allocate(SIZE);
        this.m_cursorHdr = new ASTCursorPktHdr();
        this.m_vidClnt = jVVideo;
        this.m_vidClnt.SetMouseReader(this);
        this.m_pImageBuffer = jVVideo.getImageBuffer();
        this.m_view = JViewerApp.getInstance().getRCView();
        this.m_prevCursorRect = new Rectangle(0, 0, 0, 0);
        this.m_currCursorRect = new Rectangle(0, 0, 64, 64);
    }

    public void setImageBuffer(byte[] bArr) {
        this.m_pImageBuffer = bArr;
    }

    public void set(ASTCursorPktHdr aSTCursorPktHdr) {
        this.m_cursorHdr = aSTCursorPktHdr;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public void initialize(int i) {
        this.m_ix = 0;
        this.m_mouseBuf.clear();
        this.m_mouseBuf.position(0);
        this.m_mouseBuf.limit(SIZE);
        this.m_pktSize = i;
    }

    @Override // com.ami.kvm.jviewer.hid.KVMReader
    public int read(SocketChannel socketChannel) {
        try {
            if (this.m_pktSize > 0) {
                while (this.m_ix < this.m_pktSize) {
                    this.m_ix += socketChannel.read(this.m_mouseBuf);
                }
                this.m_mouseBuf.rewind();
                this.m_mouseBuf.order(ByteOrder.LITTLE_ENDIAN);
                this.m_mouseBuf.asShortBuffer().get(this.m_pattern);
            }
            if (this.m_cursorHdr.xpos < this.m_view.viewWidth() || this.m_cursorHdr.ypos < this.m_view.viewHeight()) {
                prevremotecurpkt = true;
            }
            HeaderReader hdrReader = this.m_client.getHdrReader();
            hdrReader.initialize(0);
            this.m_client.setState(hdrReader);
            return 0;
        } catch (Exception e) {
            Debug.out.println("MOUSE_RDR " + e);
            return -1;
        }
    }

    public void DrawCursor() {
        int i = 64;
        int viewWidth = this.m_view.viewWidth();
        int viewHeight = this.m_view.viewHeight();
        if (this.m_cursorHdr.ypos > 1200) {
            this.m_cursorHdr.ypos = 0;
        }
        if (!this.m_firstTime) {
            this.m_vidClnt.RestoreScreenContent(this.m_prevCursorRect);
            this.m_view.repaint(this.m_prevCursorRect);
        } else {
            this.m_firstTime = false;
        }
        this.m_currCursorRect.setLocation(this.m_cursorHdr.xpos, this.m_cursorHdr.ypos);
        int i2 = viewWidth - this.m_cursorHdr.xpos < 64 ? viewWidth - this.m_cursorHdr.xpos : 64;
        if (viewHeight - this.m_cursorHdr.ypos < 64) {
            i = viewHeight - this.m_cursorHdr.ypos;
        }
        this.m_currCursorRect.setSize(i2, i);
        this.m_vidClnt.SaveScreenContent(this.m_currCursorRect);
        this.m_prevCursorRect.setBounds(this.m_currCursorRect);
        for (int i3 = 0; i3 < 64 - this.m_cursorHdr.yoffset; i3++) {
            if (this.m_cursorHdr.ypos + i3 < viewHeight) {
                for (int i4 = 0; i4 < 64 - this.m_cursorHdr.xoffset; i4++) {
                    if (this.m_cursorHdr.xpos + i4 < viewWidth) {
                        int i5 = (((this.m_cursorHdr.ypos + i3) * viewWidth) + this.m_cursorHdr.xpos + i4) * 3;
                        this.CursorData = this.m_pattern[((this.m_cursorHdr.yoffset + i3) * 64) + this.m_cursorHdr.xoffset + i4];
                        this.CursorColorData[0] = (byte) ((this.CursorData & 3840) >> 4);
                        this.CursorColorData[1] = (byte) (this.CursorData & 240);
                        this.CursorColorData[2] = (byte) ((this.CursorData & 15) << 4);
                        if (this.m_cursorHdr.cursorType == 1) {
                            this.Alpha = (this.CursorData & 61440) >> 12;
                            this.m_pImageBuffer[i5 + 2] = (byte) ((((15 - this.Alpha) * this.m_pImageBuffer[i5 + 2]) / 15) + ((this.Alpha * this.CursorColorData[0]) / 15));
                            this.m_pImageBuffer[i5 + 1] = (byte) ((((15 - this.Alpha) * this.m_pImageBuffer[i5 + 1]) / 15) + ((this.Alpha * this.CursorColorData[1]) / 15));
                            this.m_pImageBuffer[i5] = (byte) ((((15 - this.Alpha) * this.m_pImageBuffer[i5]) / 15) + ((this.Alpha * this.CursorColorData[2]) / 15));
                        } else {
                            this.ANDBit = (byte) ((this.CursorData & CfgBandwidth.BANDWIDTH_256KBPS) >> 15);
                            this.XORBit = (byte) ((this.CursorData & 16384) >> 14);
                            if (this.ANDBit == 0) {
                                this.m_pImageBuffer[i5 + 2] = this.CursorColorData[0];
                                this.m_pImageBuffer[i5 + 1] = this.CursorColorData[1];
                                this.m_pImageBuffer[i5 + 0] = this.CursorColorData[2];
                            } else if (this.XORBit != 0) {
                                this.m_pImageBuffer[i5 + 2] = (byte) (this.m_pImageBuffer[i5 + 2] ^ -1);
                                this.m_pImageBuffer[i5 + 1] = (byte) (this.m_pImageBuffer[i5 + 1] ^ -1);
                                this.m_pImageBuffer[i5] = (byte) (this.m_pImageBuffer[i5] ^ -1);
                            }
                        }
                    }
                }
            }
        }
        this.m_view.repaint(this.m_currCursorRect);
    }
}
