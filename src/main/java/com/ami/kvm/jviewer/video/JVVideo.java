package com.ami.kvm.jviewer.video;

import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.JViewerView;
import com.ami.kvm.jviewer.hid.MouseReader;
import java.awt.Component;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class JVVideo {
    public static final int MAX_X_RESOLUTION = 1500;
    public static final int MAX_Y_RESOLUTION = 1500;
    private MouseReader m_MouseReader;
    private byte[] m_alovicaBuf;
    private boolean m_bIsValidMouseReader = false;
    private Decoder m_decode;
    private Timer m_frTmr;
    private FrameHdr m_frameHdr = new FrameHdr();
    private int m_frameRate;
    private VideoEngineInfo m_myVEI;
    public byte[] m_prevCursorFrame;
    private Rectangle m_prevCursorRect;
    private short[][] m_screenBuf = new short[2][];
    private JViewerView m_view = JViewerApp.getInstance().getRCView();

    public void SetMouseReader(MouseReader mouseReader) {
        this.m_MouseReader = mouseReader;
        this.m_bIsValidMouseReader = true;
    }

    public JVVideo() {
        this.m_screenBuf[0] = new short[2250000];
        this.m_screenBuf[1] = new short[2250000];
        this.m_alovicaBuf = this.m_view.imageMemoryByte();
        Arrays.fill(this.m_alovicaBuf, (byte) 0);
        this.m_decode = new Decoder(this.m_view, this.m_alovicaBuf, this);
        this.m_prevCursorFrame = new byte[12288];
        this.m_frameRate = 0;
        this.m_frTmr = new Timer();
        this.m_frTmr.schedule(new FrameRateTask(), 0, 1000);
        this.m_prevCursorRect = new Rectangle(0, 0, 0, 0);
    }

    public void SetPointInSavedScreen(int i, int i2, byte b, byte b2, byte b3) {
        if (this.m_prevCursorRect.contains(i, i2)) {
            int i3 = (((i2 - this.m_prevCursorRect.y) * this.m_prevCursorRect.width) + (i - this.m_prevCursorRect.x)) * 3;
            this.m_prevCursorFrame[i3] = b;
            this.m_prevCursorFrame[i3 + 1] = b2;
            this.m_prevCursorFrame[i3 + 2] = b3;
        }
    }

    public void SaveScreenContent(Rectangle rectangle) {
        int i = 0;
        for (int i2 = 0; i2 < rectangle.height; i2++) {
            for (int i3 = 0; i3 < rectangle.width; i3++) {
                int viewWidth = (((rectangle.y + i2) * this.m_view.viewWidth()) + rectangle.x + i3) * 3;
                int i4 = i + 1;
                this.m_prevCursorFrame[i] = this.m_alovicaBuf[viewWidth];
                int i5 = i4 + 1;
                this.m_prevCursorFrame[i4] = this.m_alovicaBuf[viewWidth + 1];
                i = i5 + 1;
                this.m_prevCursorFrame[i5] = this.m_alovicaBuf[viewWidth + 2];
            }
        }
        this.m_prevCursorRect.setBounds(rectangle);
    }

    public void RestoreScreenContent(Rectangle rectangle) {
        int i = 0;
        for (int i2 = 0; i2 < rectangle.height; i2++) {
            for (int i3 = 0; i3 < rectangle.width; i3++) {
                int viewWidth = (((rectangle.y + i2) * this.m_view.viewWidth()) + rectangle.x + i3) * 3;
                int i4 = i + 1;
                this.m_alovicaBuf[viewWidth] = this.m_prevCursorFrame[i];
                int i5 = i4 + 1;
                this.m_alovicaBuf[viewWidth + 1] = this.m_prevCursorFrame[i4];
                i = i5 + 1;
                this.m_alovicaBuf[viewWidth + 2] = this.m_prevCursorFrame[i5];
            }
        }
    }

    public void onNewFrame(ByteBuffer byteBuffer) {
        this.m_frameRate++;
        byte[] bArr = this.m_frameHdr.set(byteBuffer);
        if (!this.m_frameHdr.IsValidSignature()) {
            JOptionPane.showMessageDialog((Component) null, "InValid Signature");
            return;
        }
        this.m_myVEI = this.m_frameHdr.assignASPVariables();
        if (!(this.m_view.viewWidth() == this.m_myVEI.SourceModeInfo.X && this.m_view.viewHeight() == this.m_myVEI.SourceModeInfo.Y)) {
            onResolutionChange();
        }
        this.m_decode.init_jpg_table();
        this.m_decode.decode(this.m_myVEI, MakeIntArray(bArr));
        if (this.m_bIsValidMouseReader && MouseReader.prevremotecurpkt) {
            this.m_MouseReader.DrawCursor();
        }
        this.m_view.repaint();
    }

    public int[] MakeIntArray(byte[] bArr) {
        byte[] bArr2;
        int i = 0;
        if (bArr.length % 4 == 3) {
            bArr2 = new byte[(bArr.length + 1)];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            bArr2[bArr2.length - 1] = 0;
        } else if (bArr.length % 4 == 2) {
            bArr2 = new byte[(bArr.length + 2)];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            bArr2[bArr2.length - 2] = 0;
            bArr2[bArr2.length - 1] = 0;
        } else if (bArr.length % 4 == 1) {
            bArr2 = new byte[(bArr.length + 3)];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            bArr2[bArr2.length - 3] = 0;
            bArr2[bArr2.length - 2] = 0;
            bArr2[bArr2.length - 1] = 0;
        } else {
            bArr2 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        }
        int[] iArr = new int[(bArr2.length / 4)];
        for (int i2 = 0; i2 < bArr2.length / 4; i2++) {
            iArr[i2] = (bArr2[i + 3] & 255) << 24;
            iArr[i2] = iArr[i2] | ((bArr2[i + 2] & 255) << 16);
            iArr[i2] = iArr[i2] | ((bArr2[i + 1] & 255) << 8);
            iArr[i2] = iArr[i2] | ((bArr2[i + 0] & 255) << 0);
            i += 4;
        }
        return iArr;
    }

    public void onResolutionChange() {
        this.m_view.setRCView(this.m_myVEI.SourceModeInfo.X, this.m_myVEI.SourceModeInfo.Y);
        this.m_alovicaBuf = this.m_view.imageMemoryByte();
        this.m_decode.setBuf(this.m_alovicaBuf);
        if (this.m_bIsValidMouseReader) {
            this.m_MouseReader.setImageBuffer(this.m_alovicaBuf);
        }
        this.m_view.syncCursor();
        System.gc();
    }

    public void onBlankScreen() {
        Arrays.fill(this.m_screenBuf[0], (short) 0);
        Arrays.fill(this.m_screenBuf[1], (short) 0);
        Arrays.fill(this.m_alovicaBuf, (byte) 0);
        this.m_view.repaint();
    }

    public void reset() {
        Arrays.fill(this.m_alovicaBuf, (byte) 0);
        this.m_view.repaint();
        this.m_frTmr.cancel();
        JViewerApp.getInstance().setAppWndLabel("0 fps");
    }

    public void refresh() {
        this.m_frTmr = new Timer();
        this.m_frTmr.schedule(new FrameRateTask(), 0, 1000);
    }

    /* access modifiers changed from: package-private */
    public class FrameRateTask extends TimerTask {
        FrameRateTask() {
        }

        public void run() {
            JViewerApp.getInstance().setAppWndLabel(JVVideo.this.m_frameRate + " fps");
            JVVideo.this.m_frameRate = 0;
        }
    }

    public byte[] getImageBuffer() {
        return this.m_alovicaBuf;
    }
}
