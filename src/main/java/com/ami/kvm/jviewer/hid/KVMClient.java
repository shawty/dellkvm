package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.communication.Client;
import com.ami.kvm.jviewer.communication.NWListener;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.VideoEngineConfigsDlg;
import com.ami.kvm.jviewer.kvmpkts.ASTCursorPktHdr;
import com.ami.kvm.jviewer.kvmpkts.AutoDetectBw;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.kvmpkts.CfgColorGains;
import com.ami.kvm.jviewer.kvmpkts.CfgQualityLvl;
import com.ami.kvm.jviewer.kvmpkts.CfgVideoCompression;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.kvmpkts.PS2Message;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import com.ami.kvm.jviewer.kvmpkts.VideoEngineConfigs;
import com.ami.kvm.jviewer.video.ColorGains;
import com.ami.kvm.jviewer.video.JVVideo;
import java.awt.Component;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

public class KVMClient extends Client {
    public static final int ERR_MAX_SESSION = 6;
    public static final int INVALID_CDROM_SESSION_TOKEN = 4;
    public static final int INVALID_FLOPPY_SESSION_TOKEN = 5;
    public static final int INVALID_SESSION = 0;
    public static final int INVALID_VIDEO_SESSION_TOKEN = 3;
    public static final int MAX_FRAGMENT_SIZE = 3840000;
    public static final int MAX_SCREEN_HEIGHT = 1200;
    public static final int MAX_SCREEN_WIDTH = 1600;
    public static final int STOP_SESSION_IMMEDIATE = 2;
    public static final int VALID_SESSION = 0;
    private NWListener m_NWListener;
    private volatile Thread m_NWThread;
    public ByteBuffer m_ctrlMsg;
    private CtrlReader m_ctrlReader;
    private ASTCursorPatternPkt m_curpat;
    private ASTCursorHdrReader m_cursorReader;
    private boolean m_encrypt = false;
    public short m_fragNum;
    private FragNumReader m_fragNumReader;
    private FragReader m_fragReader;
    private HeaderReader m_hdrReader;
    private KMCrypt m_kmCrypt;
    private MouseReader m_mouseReader;
    private NullReader m_nullReader;
    private boolean m_pause = false;
    public IVTPPktHdr m_pktHdr;
    private KVMReader m_reader;
    private boolean m_redirection = false;
    private long m_startTS;
    private long m_stopTS;
    private JVVideo m_vidClnt;
    private VideoEngineConfigsDlg m_vidconfigdlg;

    public KVMClient(byte[] bArr, int i, JVVideo jVVideo) {
        super(bArr, i);
        this.m_vidClnt = jVVideo;
        this.m_hdrReader = new HeaderReader(this);
        this.m_fragNumReader = new FragNumReader(this);
        this.m_fragReader = new FragReader(this);
        this.m_ctrlReader = new CtrlReader(this);
        this.m_nullReader = new NullReader(this);
        this.m_mouseReader = new MouseReader(this, jVVideo);
        this.m_cursorReader = new ASTCursorHdrReader(this);
        this.m_curpat = new ASTCursorPatternPkt();
        this.m_reader = this.m_hdrReader;
        this.m_kmCrypt = new KMCrypt();
    }

    @Override // com.ami.kvm.jviewer.communication.Client
    public final void onReadEvent() {
        this.m_reader.read(this.m_sockCh);
    }

    public final void setState(KVMReader kVMReader) {
        this.m_reader = kVMReader;
    }

    public final HeaderReader getHdrReader() {
        return this.m_hdrReader;
    }

    public void handleCursorData() {
        System.out.println("handleCursorData");
        int i = 0;
        ByteBuffer wrap = ByteBuffer.wrap(new byte[ASTCursorPatternPkt.ASTCURPAT_PKT_SIZE]);
        while (wrap.hasRemaining()) {
            try {
                i += this.m_sockCh.read(wrap);
            } catch (Exception e) {
                return;
            }
        }
        System.out.println("curPat Info" + i);
        if (8249 <= i) {
            this.m_curpat.setASTCursorPattern(wrap);
        }
    }

    public FragNumReader getFragNumReader() {
        return this.m_fragNumReader;
    }

    public FragReader getFragReader() {
        return this.m_fragReader;
    }

    public CtrlReader getCtrlReader() {
        return this.m_ctrlReader;
    }

    public NullReader getNullReader() {
        return this.m_nullReader;
    }

    public ASTCursorHdrReader getCursorReader() {
        return this.m_cursorReader;
    }

    public MouseReader getMouseReader(ASTCursorPktHdr aSTCursorPktHdr) {
        this.m_mouseReader.set(aSTCursorPktHdr);
        return this.m_mouseReader;
    }

    public void onNewFrame(ByteBuffer byteBuffer) {
        try {
            this.m_vidClnt.onNewFrame(byteBuffer);
        } catch (Exception e) {
        }
    }

    public void onControlMessage() {
        switch (this.m_pktHdr.type) {
            case 15:
                Debug.out.println("*** Blank screen");
                this.m_vidClnt.onBlankScreen();
                return;
            case 23:
                Debug.out.println("*** Color gain");
                this.m_ctrlMsg.flip();
                JViewerApp.getInstance().onColorGains(new ColorGains(this.m_ctrlMsg));
                return;
            case 25:
                Debug.out.println("*** Stop session");
                JViewerApp.getInstance().OnVideoStopRedirection();
                JOptionPane.showMessageDialog((Component) null, "Stop session message received. \nStopping redirection!", "Session stopped", 1);
                return;
            case 28:
                Debug.out.println("*** Encryption Key");
                return;
            case 29:
                Debug.out.println("*** Encryption Status");
                JViewerApp.getInstance().OnEncryptionStatus();
                return;
            case 30:
                Debug.out.println("*** Initial Encryption Status");
                JViewerApp.getInstance().OnInitialEncryptionStatus();
                return;
            case 33:
                Debug.out.println("*** Get Mouse Mode Response");
                this.m_ctrlMsg.flip();
                JViewerApp.getInstance().OnGetMouseMode(this.m_ctrlMsg.get());
                return;
            case 35:
                Debug.out.println("*** ADVISER_VALIDATE_VIDEO_SESSION_RESPONSE");
                this.m_ctrlMsg.flip();
                JViewerApp.getInstance().OnValidateVideoSessionResp(this.m_ctrlMsg.get());
                return;
            case 37:
            case 39:
                return;
            case 41:
                VideoEngineConfigs videoEngineConfigs = new VideoEngineConfigs();
                videoEngineConfigs.setVideoEngineConfigs(this.m_ctrlMsg);
                JViewerApp.getInstance().getVidCfgDlg().setVideoConfigs(videoEngineConfigs);
                return;
            case 48:
                System.out.println("ADVISER_AST2K_ADD_NEW_MOUSE_CURSOR\n");
                handleCursorData();
                return;
            case 49:
                System.out.println("MAX NUM SESSION REACHED\n");
                JOptionPane.showMessageDialog((Component) null, "Max  session received. \nStopping redirection!", "Session stopped", 1);
                return;
            default:
                Debug.out.println("*** Invalid packet");
                return;
        }
    }

    public boolean redirection() {
        return this.m_redirection;
    }

    public boolean pauseFlag() {
        return this.m_pause;
    }

    public int startRedirection() {
        if (this.m_redirection) {
            return 0;
        }
        try {
            if (-1 == connect()) {
                return -1;
            }
        } catch (IOException e) {
            Debug.out.println("Failed to establish connection");
        }
        this.m_NWListener = new NWListener();
        if (-1 == this.m_NWListener.addChannel(this)) {
            return -1;
        }
        this.m_NWThread = new Thread(this.m_NWListener, "listener");
        this.m_NWListener.startListener();
        this.m_NWThread.start();
        return DoLogin();
    }

    public int DoLogin() {
        IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_VALIDATE_VIDEO_SESSION, 16, 0);
        String sessionToken = JViewerApp.getInstance().getSessionToken();
        ByteBuffer allocate = ByteBuffer.allocate(iVTPPktHdr.size() + 16);
        byte[] bArr = new byte[16];
        try {
            bArr = MessageDigest.getInstance("MD5").digest(sessionToken.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Failed to create hashed session token ");
            System.exit(0);
        }
        KMCrypt kMCrypt = this.m_kmCrypt;
        byte[] bytes = sessionToken.getBytes();
        KMCrypt kMCrypt2 = this.m_kmCrypt;
        kMCrypt.initialize(bytes, 1);
        allocate.position(0);
        allocate.put(iVTPPktHdr.array());
        allocate.put(sessionToken.getBytes());
        if (bArr.length > 16) {
            Debug.out.println("Session token length is greater than defined " + sessionToken);
        }
        for (int length = sessionToken.getBytes().length; length < 16; length++) {
            allocate.put((byte) 0);
        }
        allocate.position(0);
        byte[] bArr2 = new byte[allocate.limit()];
        allocate.get(bArr2);
        if (bArr2.length == sendMessage(bArr2, bArr2.length)) {
            return 0;
        }
        Debug.out.println("Failed to send Session token to the card");
        return -1;
    }

    public int OnValidVideoSession() {
        setBandwidth(CfgBandwidth.BANDWIDTH_100MBPS);
        this.m_redirection = true;
        return 0;
    }

    public void stopRedirection() {
        if (this.m_redirection) {
            this.m_NWListener.removeChannel(this);
            close();
            this.m_NWListener.destroyListener();
            this.m_redirection = false;
            this.m_pause = true;
            this.m_vidClnt.reset();
            this.m_reader = this.m_hdrReader;
            this.m_reader.initialize(0);
            this.m_kmCrypt.close();
        }
    }

    public void pauseRedirection() {
        if (this.m_redirection) {
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_PAUSE_REDIRECTION, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
            this.m_vidClnt.reset();
            this.m_redirection = false;
            this.m_pause = true;
        }
    }

    public void resumeRedirection() {
        this.m_redirection = true;
        this.m_pause = false;
        this.m_vidClnt.refresh();
        if (this.m_redirection) {
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_RESUME_REDIRECTION, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public int setVideoCompressionType(byte b) {
        if (!this.m_redirection) {
            return -1;
        }
        CfgVideoCompression cfgVideoCompression = new CfgVideoCompression(b);
        if (cfgVideoCompression.size() == sendMessage(cfgVideoCompression.report(), cfgVideoCompression.size())) {
            return 0;
        }
        Debug.out.println("Failed to send Config comrpession message");
        return -1;
    }

    public int setBandwidth(int i) {
        if (!this.m_redirection) {
            return -1;
        }
        CfgBandwidth cfgBandwidth = new CfgBandwidth(i);
        if (cfgBandwidth.size() == sendMessage(cfgBandwidth.report(), cfgBandwidth.size())) {
            return 0;
        }
        Debug.out.println("Failed to send Config Bandwidth message");
        return -1;
    }

    public int setQualityLvl(byte b) {
        if (!this.m_redirection) {
            return -1;
        }
        CfgQualityLvl cfgQualityLvl = new CfgQualityLvl(b);
        if (cfgQualityLvl.size() == sendMessage(cfgQualityLvl.report(), cfgQualityLvl.size())) {
            return 0;
        }
        Debug.out.println("Failed to send Config Quality Level message");
        return -1;
    }

    public void setEncryption(boolean z) {
        IVTPPktHdr iVTPPktHdr;
        if (this.m_redirection) {
            if (z) {
                this.m_encrypt = true;
                iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_ENABLE_ENCRYPTION, 0, 0);
            } else {
                this.m_encrypt = false;
                iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_DISABLE_ENCRYPTION, 0, 0);
            }
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public boolean isKMEncryptionEnabled() {
        return this.m_encrypt;
    }

    public void notifyEncryption(boolean z) {
        if (this.m_redirection) {
            this.m_encrypt = z;
        }
    }

    public int setColorGains(byte b, byte b2, byte b3) {
        if (!this.m_redirection) {
            return -1;
        }
        CfgColorGains cfgColorGains = new CfgColorGains(b, b2, b3);
        if (cfgColorGains.size() == sendMessage(cfgColorGains.report(), cfgColorGains.size())) {
            return 0;
        }
        Debug.out.println("Failed to send Config Color Gains message");
        return -1;
    }

    public void setDefaultColorGains() {
        if (this.m_redirection) {
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_DEFAULT_COLOR_GAIN, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public void getColorGains() {
        if (this.m_redirection) {
            Debug.out.println("requesting color gains");
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr((byte) 16, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public void getVideoEngineConfigs() {
        if (this.m_redirection) {
            Debug.out.println("requesting color gains");
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_AST2K_GET_VIDEO_ENGINE_CONFIGS, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public void moveScreen(byte b) {
        if (this.m_redirection) {
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(b, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public void autocalibrate() {
        if (this.m_redirection) {
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_AUTO_CALIBRATE, 0, 0);
            sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
        }
    }

    public int sendKMMessage(PS2Message pS2Message) {
        byte[] report;
        if (!this.m_redirection) {
            return 0;
        }
        if (!this.m_encrypt || !this.m_kmCrypt.isInitialized()) {
            report = pS2Message.report();
        } else {
            report = pS2Message.encryptedReport(this.m_kmCrypt);
        }
        sendMessage(report, report.length);
        return 0;
    }

    public int sendKMMessage(USBMessage uSBMessage) {
        byte[] report;
        if (!this.m_redirection) {
            return 0;
        }
        if (!this.m_encrypt || !this.m_kmCrypt.isInitialized()) {
            report = uSBMessage.report();
        } else {
            report = uSBMessage.encryptedReport(this.m_kmCrypt);
        }
        sendMessage(report, report.length);
        return 0;
    }

    public int autoDetect() {
        if (!this.m_redirection) {
            return -1;
        }
        AutoDetectBw autoDetectBw = new AutoDetectBw(CfgBandwidth.BANDWIDTH_100MBPS);
        if (autoDetectBw.size() == sendMessage(autoDetectBw.report(), autoDetectBw.size())) {
            return 0;
        }
        Debug.out.println("Failed to send auto detect message");
        return -1;
    }

    public void onStartReading() {
        this.m_startTS = System.currentTimeMillis();
    }

    public void onStopReading() {
        this.m_stopTS = System.currentTimeMillis();
        int determineBandwidth = CfgBandwidth.determineBandwidth(this.m_stopTS - this.m_startTS);
        Debug.out.println("Time " + (this.m_stopTS - this.m_startTS) + ", Bandwidth " + determineBandwidth);
        String str = "unknown";
        switch (determineBandwidth) {
            case CfgBandwidth.BANDWIDTH_256KBPS:
                JViewerApp.getInstance().OnOptionsBandwidth256Kbps();
                str = "256kbps";
                break;
            case CfgBandwidth.BANDWIDTH_512KBPS:
                JViewerApp.getInstance().OnOptionsBandwidth512Kbps();
                str = "512kbps";
                break;
            case CfgBandwidth.BANDWIDTH_1MBPS:
                JViewerApp.getInstance().OnOptionsBandwidth1Mbps();
                str = "1mbps";
                break;
            case CfgBandwidth.BANDWIDTH_10MBPS:
                JViewerApp.getInstance().OnOptionsBandwidth10Mbps();
                str = "10mbps";
                break;
            case CfgBandwidth.BANDWIDTH_100MBPS:
                JViewerApp.getInstance().OnOptionsBandwidth100Mbps();
                str = "100mbps";
                break;
        }
        JViewerApp.getInstance().updateBandwidthMsg(str);
    }

    public KMCrypt getCrypt() {
        return this.m_kmCrypt;
    }
}
