package com.ami.kvm.jviewer.gui;

import com.ami.iusb.IUSBRedirSession;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.hid.KVMClient;
import com.ami.kvm.jviewer.hid.MouseReader;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.kvmpkts.USBKeyboardRep;
import com.ami.kvm.jviewer.video.ColorGains;
import com.ami.kvm.jviewer.video.JVVideo;
import java.nio.ByteBuffer;
import javax.swing.JOptionPane;

public class JViewerApp {
    public static int REDIR_STARTED = 2;
    public static int REDIR_STARTING = 1;
    public static int REDIR_STOPPED = 0;
    public static int REDIR_STOPPING = 3;
    private static int m_langIndex = 0;
    private static JViewerApp m_rcApp = new JViewerApp();
    private IUSBRedirSession m_IUSBSession = new IUSBRedirSession();
    private KVMClient m_KVMClnt;
    private int m_RedirectionState = REDIR_STOPPED;
    AutoBWDlg m_autoBWDlg;
    private ColorGains m_clrGains = null;
    private String m_encToken;
    private JVFrame m_frame;
    private FSFrame m_fsFrame = new FSFrame();
    private byte[] m_serverIP;
    private int m_serverPort;
    private String m_session_token;
    private VideoSettings m_vidStgs;
    private VideoEngineConfigsDlg m_vidconfigdlg;
    private JViewerView m_view = new JViewerView(1600, 1200);
    private WindowFrame m_wndFrame = new WindowFrame();
    private boolean m_wndMode;

    public static JViewerApp getInstance() {
        return m_rcApp;
    }

    private JViewerApp() {
    }

    public JViewerView getRCView() {
        return this.m_view;
    }

    public KVMClient getKVMClient() {
        return this.m_KVMClnt;
    }

    public VideoEngineConfigsDlg getVidCfgDlg() {
        return this.m_vidconfigdlg;
    }

    public JVMenu getJVMenu() {
        return this.m_wndFrame.getMenu();
    }

    public JVFrame getMainWindow() {
        return this.m_wndFrame;
    }

    public String getToken() {
        return this.m_encToken;
    }

    public String getSessionToken() {
        return this.m_session_token;
    }

    public void setStatus(String str) {
        this.m_frame.setStatus(str);
    }

    public void resetStatus() {
        this.m_frame.resetStatus();
    }

    public boolean isFullScreenMode() {
        return !this.m_wndMode;
    }

    public void refreshAppWndLabel() {
        try {
            this.m_frame.refreshTitle();
        } catch (Exception e) {
        }
    }

    public void setAppWndLabel(String str) {
        try {
            this.m_frame.setWndLabel(str);
        } catch (Exception e) {
        }
    }

    public int GetRedirectionState() {
        return this.m_RedirectionState;
    }

    public void OnConnectToServer(byte[] bArr, int i, String str) {
        this.m_wndFrame.attachView();
        this.m_serverIP = bArr;
        this.m_serverPort = i;
        this.m_encToken = str;
        this.m_session_token = str;
        this.m_wndMode = true;
        this.m_frame = this.m_wndFrame;
        JVMenu menu = this.m_frame.getMenu();
        menu.notifyRedirection(false);
        menu.notifyFullScreen(false);
        menu.notifyMultKeyBoard(m_langIndex);
        OnVideoStartRedirection();
    }

    public void OnVideoStartRedirection() {
        this.m_RedirectionState = REDIR_STARTING;
        this.m_KVMClnt = new KVMClient(this.m_serverIP, this.m_serverPort, new JVVideo());
        if (-1 == this.m_KVMClnt.startRedirection()) {
            this.m_RedirectionState = REDIR_STOPPED;
            JOptionPane.showMessageDialog(this.m_frame, "Connection failed!", "Connection", 0);
            System.exit(0);
        }
        JVFrame.setServerIP(this.m_serverIP);
        this.m_view.addKMListeners();
        this.m_view.syncCursor();
        this.m_frame.getMenu().notifyRedirection(true);
        this.m_RedirectionState = REDIR_STARTED;
    }

    public void OnVideoStopRedirection() {
        this.m_RedirectionState = REDIR_STOPPING;
        this.m_view.removeKMListeners();
        JVFrame.setServerIP(null);
        JVMenu jVMenu = getJVMenu();
        if (JVMenu.m_stateRedirFloppy) {
            jVMenu.notifyFloppyRedir(false);
            OnFloppyRedir(false);
        }
        if (JVMenu.m_stateRedirFloppyImage) {
            jVMenu.notifyFloppyImageRedir(false);
            OnFloppyImageRedir(false);
        }
        if (JVMenu.m_stateRedirCDROM) {
            jVMenu.notifyCDROMRedir(false);
            OnCDROMRedir(false);
        }
        if (JVMenu.m_stateRedirISO) {
            jVMenu.notifyISORedir(false);
            OnISORedir(false);
        }
        if (JVMenu.m_mouseSync) {
            jVMenu.getMenuItem(JVMenu.MOUSE_SYNC_CURSOR).setSelected(false);
            OnUSBMouseSyncCursor(false);
        }
        this.m_KVMClnt.stopRedirection();
        MouseReader.prevremotecurpkt = false;
        this.m_frame.getMenu().notifyRedirection(false);
        if (isFullScreenMode()) {
            OnVideoFullScreen(false);
        }
        this.m_RedirectionState = REDIR_STOPPED;
    }

    public void OnVideoRestart() {
        OnVideoStopRedirection();
        OnVideoStartRedirection();
    }

    public void OnVideoPauseRedirection() {
        Debug.out.println("OnVideoStopRedirection");
        this.m_RedirectionState = REDIR_STOPPING;
        JVFrame.setServerIP(null);
        this.m_KVMClnt.pauseRedirection();
        this.m_frame.getMenu().notifyRedirection(false);
        this.m_RedirectionState = REDIR_STOPPED;
    }

    public void OnVideoResumeRedirection() {
        Debug.out.println("OnVideoResume");
        JVFrame.setServerIP(this.m_serverIP);
        getInstance().refreshAppWndLabel();
        this.m_RedirectionState = REDIR_STARTING;
        this.m_KVMClnt.resumeRedirection();
        this.m_frame.getMenu().notifyRedirection(true);
        this.m_RedirectionState = REDIR_STARTED;
    }

    public void OnVideoRefreshRedirection() {
        Debug.out.println("OnVideoRefresh");
        OnVideoPauseRedirection();
        OnVideoResumeRedirection();
    }

    public void OnVideoFullScreen(boolean z) {
        if (this.m_wndMode) {
            this.m_wndFrame.detachView();
            this.m_fsFrame.attachView();
            this.m_frame = this.m_fsFrame;
            this.m_wndMode = false;
            getInstance().refreshAppWndLabel();
        } else {
            this.m_fsFrame.detachView();
            this.m_wndFrame.attachView();
            this.m_frame = this.m_wndFrame;
            this.m_wndMode = true;
        }
        this.m_frame.getMenu().notifyFullScreen(z);
        this.m_frame.getMenu().refreshMenu();
    }

    public void OnSetLangIndex(int i) {
        m_langIndex = i;
    }

    public static int getLangIndex() {
        return m_langIndex;
    }

    public void OnVideoExit() {
        this.m_frame.exitApp();
    }

    public void OnKeyboardHoldRightCtrlKey(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(17, 3, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyRCtrl(z);
        }
    }

    public void OnKeyboardHoldRightAltKey(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(18, 3, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyRAlt(z);
        }
    }

    public void OnKeyboardHoldLeftCtrlKey(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(17, 2, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyLCtrl(z);
        }
    }

    public void OnKeyboardHoldLeftAltKey(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(18, 2, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyLAlt(z);
        }
    }

    public void OnKeyboardLeftWindowsKeyHoldDown(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(524, 2, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyLWinDown(z);
        }
    }

    public void OnKeyboardLeftWindowsKeyPressRelease() {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(524, 2, true);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(524, 2, false);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
        }
    }

    public void OnKeyboardRightWindowsKeyHoldDown(boolean z) {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(524, 3, z);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            this.m_frame.getMenu().notifyRWinDown(z);
        }
    }

    public void OnKeyboardRightWindowsKeyPressRelease() {
        if (this.m_KVMClnt.redirection()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(524, 3, true);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(524, 3, false);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
        }
    }

    public void OnKeyboardAltCtrlDel() {
        if (this.m_KVMClnt.redirection() && !this.m_frame.getMenu().getRCtrlState() && !this.m_frame.getMenu().getLCtrlState()) {
            USBKeyboardRep uSBKeyboardRep = new USBKeyboardRep();
            uSBKeyboardRep.set(17, 2, true);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(18, 2, true);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(127, 1, true);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(127, 1, false);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(18, 2, false);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
            uSBKeyboardRep.set(17, 2, false);
            this.m_KVMClnt.sendKMMessage(uSBKeyboardRep);
        }
    }

    public void OnKeyboardFullKeyboard(boolean z) {
        getJVMenu().notifyFullKeyboard(z);
        if (z) {
            JViewerView jViewerView = this.m_view;
            JViewerView.m_isFullKybrd = true;
        } else {
            JViewerView jViewerView2 = this.m_view;
            JViewerView.m_isFullKybrd = false;
        }
        Debug.out.println("OnKeyboardFullKeyboard");
    }

    public void OnUSBMouseSyncCursor(boolean z) {
        getJVMenu().notifyMouseCursor(z);
        boolean USBsyncCursor = this.m_view.USBsyncCursor(z);
        if (z && !USBsyncCursor) {
            this.m_frame.getMenu().getMenuItem(JVMenu.MOUSE_SYNC_CURSOR).setSelected(false);
        }
    }

    public void OnMouseSyncCursor() {
        this.m_view.syncCursor();
    }

    public void OnOptionsBandwidthAutoDetect() {
        Debug.out.println("OnOptionsBandwidthAutoDetect");
        this.m_KVMClnt.autoDetect();
        this.m_autoBWDlg = new AutoBWDlg(this.m_frame);
        this.m_autoBWDlg.setVisible(true);
    }

    public void OnOptionsBandwidth256Kbps() {
        Debug.out.println("OnOptionsBandwidth256Kbps");
        this.m_KVMClnt.setBandwidth(CfgBandwidth.BANDWIDTH_256KBPS);
        this.m_frame.getMenu().notifyBandwidth(JVMenu.OPTIONS_BANDWIDTH_256KBPS);
    }

    public void OnOptionsBandwidth512Kbps() {
        Debug.out.println("OnOptionsBandwidth512Kbps");
        this.m_KVMClnt.setBandwidth(CfgBandwidth.BANDWIDTH_512KBPS);
        this.m_frame.getMenu().notifyBandwidth(JVMenu.OPTIONS_BANDWIDTH_512KBPS);
    }

    public void OnOptionsBandwidth1Mbps() {
        Debug.out.println("OnOptionsBandwidth1Mbps");
        this.m_KVMClnt.setBandwidth(CfgBandwidth.BANDWIDTH_1MBPS);
        this.m_frame.getMenu().notifyBandwidth(JVMenu.OPTIONS_BANDWIDTH_1MBPS);
    }

    public void OnOptionsBandwidth10Mbps() {
        Debug.out.println("OnOptionsBandwidth10Mbps");
        this.m_KVMClnt.setBandwidth(CfgBandwidth.BANDWIDTH_10MBPS);
        this.m_frame.getMenu().notifyBandwidth(JVMenu.OPTIONS_BANDWIDTH_10MBPS);
    }

    public void OnOptionsBandwidth100Mbps() {
        Debug.out.println("OnOptionsBandwidth100Mbps");
        this.m_KVMClnt.setBandwidth(CfgBandwidth.BANDWIDTH_100MBPS);
        this.m_frame.getMenu().notifyBandwidth(JVMenu.OPTIONS_BANDWIDTH_100MBPS);
    }

    public void updateBandwidthMsg(String str) {
        if (this.m_autoBWDlg != null && this.m_autoBWDlg.isVisible()) {
            this.m_autoBWDlg.setMessage("Detected bandwidth - " + str);
        }
        this.m_autoBWDlg.done();
    }

    public void OnOptionsVideoSettings() {
        Debug.out.println("OnOptionsVideoSettings");
        if (this.m_KVMClnt == null || !this.m_KVMClnt.redirection()) {
            JOptionPane.showMessageDialog(this.m_frame, "Start redirection and try again.", "Video settings", 1);
        } else {
            this.m_vidconfigdlg = new VideoEngineConfigsDlg();
        }
    }

    public void OnHelpAboutJViewer() {
        Debug.out.println("OnHelpAboutJViewer");
        JOptionPane.showMessageDialog(this.m_frame, "KVM Remote Console Utility Version 1.10\nCopyright (c) 2006 American Megatrends, Inc.", "About JViewer", 1);
    }

    public void onColorGains(ColorGains colorGains) {
        this.m_clrGains = colorGains;
    }

    public void OnInitialEncryptionStatus() {
        this.m_KVMClnt.notifyEncryption(true);
    }

    public void OnEncryptionStatus() {
        if (!this.m_KVMClnt.isKMEncryptionEnabled()) {
            this.m_KVMClnt.notifyEncryption(true);
            JOptionPane.showMessageDialog(this.m_frame, "Keyboard/mouse encryption is enabled by other sessions.\nEnabling encryption for this session", "Keyboard/mouse encryption", 1);
        }
    }

    public void initializeCrypt(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[16];
        if (!this.m_KVMClnt.getCrypt().isInitialized()) {
            for (int i = 0; i < 16; i++) {
                bArr[i] = byteBuffer.get(i + 19);
            }
            this.m_KVMClnt.getCrypt().initialize(bArr, this.m_encToken, "$AMI$", 1);
        }
    }

    public void OnGetMouseMode(byte b) {
        Debug.out.println("Mouse mode response packet received");
        this.m_view.SetUSBMouseMode(b);
    }

    public void OnCDROMRedir(boolean z) {
        boolean z2 = true;
        JVMenu jVMenu = getJVMenu();
        if (!this.m_KVMClnt.pauseFlag() && !this.m_KVMClnt.redirection()) {
            jVMenu.notifyCDROMRedir(false);
        } else if (!this.m_IUSBSession.Is_CDROMRedirCmdStatus_FREE()) {
            this.m_frame.generalErrorMessage("CDROM Redirection", "Device busy. Please try after some time");
            if (z) {
                z2 = false;
            }
            jVMenu.notifyCDROMRedir(z2);
            this.m_frame.getMenu().refreshMenu();
        } else {
            IUSBRedirSession iUSBRedirSession = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession2 = this.m_IUSBSession;
            iUSBRedirSession.SetCDROMRedirCmdStatus(1);
            if (!z) {
                if (!this.m_IUSBSession.StopCDROMRedir()) {
                    jVMenu.notifyCDROMRedir(true);
                }
                IUSBRedirSession iUSBRedirSession3 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession4 = this.m_IUSBSession;
                iUSBRedirSession3.SetCDROMRedirCmdStatus(2);
            } else if (JVMenu.m_stateRedirISO) {
                this.m_frame.generalErrorMessage("Cannot redirect CD-ROM", "CD-ROM redirection cannot be started until ISO redirection is stopped");
                jVMenu.notifyCDROMRedir(false);
                this.m_frame.getMenu().refreshMenu();
                IUSBRedirSession iUSBRedirSession5 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession6 = this.m_IUSBSession;
                iUSBRedirSession5.SetCDROMRedirCmdStatus(2);
                return;
            } else {
                if (!this.m_IUSBSession.StartCDROMRedir()) {
                    Debug.out.println("Start CDROM Redirection failed\n");
                    jVMenu.notifyCDROMRedir(false);
                    this.m_frame.getMenu().refreshMenu();
                } else {
                    Debug.out.println("Start CDROM Redirection successful");
                }
                IUSBRedirSession iUSBRedirSession7 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession8 = this.m_IUSBSession;
                iUSBRedirSession7.SetCDROMRedirCmdStatus(2);
            }
            this.m_frame.getMenu().refreshMenu();
        }
    }

    public void OnISORedir(boolean z) {
        boolean z2 = true;
        JVMenu jVMenu = getJVMenu();
        if (!this.m_KVMClnt.pauseFlag() && !this.m_KVMClnt.redirection()) {
            jVMenu.notifyISORedir(false);
        } else if (!this.m_IUSBSession.Is_ISORedirCmdStatus_FREE()) {
            this.m_frame.generalErrorMessage("ISO Redirection", "Device busy. Please try after some time");
            if (z) {
                z2 = false;
            }
            jVMenu.notifyISORedir(z2);
            this.m_frame.getMenu().refreshMenu();
        } else {
            IUSBRedirSession iUSBRedirSession = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession2 = this.m_IUSBSession;
            iUSBRedirSession.SetISORedirCmdStatus(1);
            if (!z) {
                Debug.out.println("OnISORedir = false \n");
                if (!this.m_IUSBSession.StopISORedir()) {
                    jVMenu.notifyISORedir(true);
                }
                IUSBRedirSession iUSBRedirSession3 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession4 = this.m_IUSBSession;
                iUSBRedirSession3.SetISORedirCmdStatus(2);
            } else if (JVMenu.m_stateRedirCDROM) {
                this.m_frame.generalErrorMessage("Cannot redirect ISO image", "CD-ROM image redirection cannot be started until normal CD-ROM redirection is stopped");
                jVMenu.notifyISORedir(false);
                this.m_frame.getMenu().refreshMenu();
                IUSBRedirSession iUSBRedirSession5 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession6 = this.m_IUSBSession;
                iUSBRedirSession5.SetISORedirCmdStatus(2);
                return;
            } else {
                if (!this.m_IUSBSession.StartISORedir()) {
                    Debug.out.println("Start ISO redirection failed");
                    jVMenu.notifyISORedir(false);
                    this.m_frame.getMenu().refreshMenu();
                } else {
                    Debug.out.println("Start ISO redirection successfull");
                }
                IUSBRedirSession iUSBRedirSession7 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession8 = this.m_IUSBSession;
                iUSBRedirSession7.SetISORedirCmdStatus(2);
            }
            this.m_frame.getMenu().refreshMenu();
        }
    }

    public void OnFloppyRedir(boolean z) {
        boolean z2 = true;
        JVMenu jVMenu = getJVMenu();
        if (JVMenu.m_stateRedirFloppyImage) {
            this.m_frame.generalErrorMessage("Cannot redirect Floppy", "Floppy redirection cannot be started until Floppy image redirection is stopped");
            jVMenu.notifyFloppyRedir(false);
            this.m_frame.getMenu().refreshMenu();
            IUSBRedirSession iUSBRedirSession = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession2 = this.m_IUSBSession;
            iUSBRedirSession.SetFloppyRedirCmdStatus(2);
        } else if (this.m_KVMClnt.pauseFlag() || this.m_KVMClnt.redirection()) {
            if (this.m_IUSBSession.floppySession != null) {
                this.m_IUSBSession.StopFloppyRedir();
                this.m_IUSBSession.floppySession = null;
            }
            if (!this.m_IUSBSession.Is_FloppyRedirCmdStatus_FREE()) {
                this.m_frame.generalErrorMessage("Floppy Redirection", "Device busy. Please try after some time");
                if (z) {
                    z2 = false;
                }
                jVMenu.notifyFloppyRedir(z2);
                this.m_frame.getMenu().refreshMenu();
                return;
            }
            IUSBRedirSession iUSBRedirSession3 = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession4 = this.m_IUSBSession;
            iUSBRedirSession3.SetFloppyRedirCmdStatus(1);
            if (z) {
                if (!this.m_IUSBSession.StartFloppyRedir()) {
                    jVMenu.notifyFloppyRedir(false);
                }
                IUSBRedirSession iUSBRedirSession5 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession6 = this.m_IUSBSession;
                iUSBRedirSession5.SetFloppyRedirCmdStatus(2);
            } else {
                if (!this.m_IUSBSession.StopFloppyRedir()) {
                    jVMenu.notifyFloppyRedir(true);
                }
                IUSBRedirSession iUSBRedirSession7 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession8 = this.m_IUSBSession;
                iUSBRedirSession7.SetFloppyRedirCmdStatus(2);
            }
            this.m_frame.getMenu().refreshMenu();
        } else {
            jVMenu.notifyFloppyRedir(false);
        }
    }

    public void OnFloppyImageRedir(boolean z) {
        boolean z2 = true;
        JVMenu jVMenu = getJVMenu();
        if (JVMenu.m_stateRedirFloppy) {
            this.m_frame.generalErrorMessage("Cannot redirect Floppy image", "Floppy image redirection cannot be started until normal floppy redirection is stopped");
            jVMenu.notifyFloppyImageRedir(false);
            this.m_frame.getMenu().refreshMenu();
            IUSBRedirSession iUSBRedirSession = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession2 = this.m_IUSBSession;
            iUSBRedirSession.SetFloppyImageRedirCmdStatus(2);
        } else if (this.m_KVMClnt.pauseFlag() || this.m_KVMClnt.redirection()) {
            if (this.m_IUSBSession.floppySession != null) {
                this.m_IUSBSession.StopFloppyImageRedir();
                this.m_IUSBSession.floppySession = null;
            }
            if (!this.m_IUSBSession.Is_FloppyImageRedirCmdStatus_FREE()) {
                this.m_frame.generalErrorMessage("Floppy Image Redirection", "Device busy. Please try after some time");
                if (z) {
                    z2 = false;
                }
                jVMenu.notifyFloppyImageRedir(z2);
                this.m_frame.getMenu().refreshMenu();
                return;
            }
            IUSBRedirSession iUSBRedirSession3 = this.m_IUSBSession;
            IUSBRedirSession iUSBRedirSession4 = this.m_IUSBSession;
            iUSBRedirSession3.SetFloppyImageRedirCmdStatus(1);
            if (z) {
                if (!this.m_IUSBSession.StartFloppyImageRedir()) {
                    jVMenu.notifyFloppyImageRedir(false);
                }
                IUSBRedirSession iUSBRedirSession5 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession6 = this.m_IUSBSession;
                iUSBRedirSession5.SetFloppyImageRedirCmdStatus(2);
            } else {
                if (!this.m_IUSBSession.StopFloppyImageRedir()) {
                    jVMenu.notifyFloppyImageRedir(true);
                }
                IUSBRedirSession iUSBRedirSession7 = this.m_IUSBSession;
                IUSBRedirSession iUSBRedirSession8 = this.m_IUSBSession;
                iUSBRedirSession7.SetFloppyImageRedirCmdStatus(2);
            }
            this.m_frame.getMenu().refreshMenu();
        } else {
            jVMenu.notifyFloppyImageRedir(false);
        }
    }

    public void OnValidateVideoSessionResp(byte b) {
        if (b == 0) {
            this.m_frame.generalErrorMessage("Authentication", "Invalid Session token. Authentication failure.");
            System.exit(0);
        }
        this.m_KVMClnt.OnValidVideoSession();
    }
}
