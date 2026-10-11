package com.ami.kvm.jviewer.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public abstract class JVMenu {
    public static final String DEVICE_CDROM_REDIRECTION = "DeviceCDROMRedirection";
    public static final String DEVICE_FLOPPY_IMAGE_REDIRECTION = "DeviceFloppyImageRedirection";
    public static final String DEVICE_FLOPPY_REDIRECTION = "DeviceFloppyRedirection";
    public static final String DEVICE_ISO_REDIRECTION = "DeviceISORedirection";
    public static final String HELP_ABOUT_RCONSOLE = "HelpAboutJViewer";
    public static final String KEYBOARD_CTRL_ALT_DEL = "KeyboardAltCtrlDel";
    public static final String KEYBOARD_FULL_KEYBOARD = "KeyboardFullKeyboard";
    public static final String KEYBOARD_LEFT_ALT_KEY = "KeyboardHoldLeftAltKey";
    public static final String KEYBOARD_LEFT_CTRL_KEY = "KeyboardHoldLeftCtrlKey";
    public static final String KEYBOARD_LEFT_WINKEY_PRESSHOLD = "KeyboardLeftWindowsKeyHoldDown";
    public static final String KEYBOARD_LEFT_WINKEY_PRESSRELEASE = "KeyboardLeftWindowsKeyPressRelease";
    public static final String KEYBOARD_RIGHT_ALT_KEY = "KeyboardHoldRightAltKey";
    public static final String KEYBOARD_RIGHT_CTRL_KEY = "KeyboardHoldRightCtrlKey";
    public static final String KEYBOARD_RIGHT_WINKEY_PRESSHOLD = "KeyboardRightWindowsKeyHoldDown";
    public static final String KEYBOARD_RIGHT_WINKEY_PRESSRELEASE = "KeyboardRightWindowsKeyPressRelease";
    public static final int LANGUAGE_ENGLISH_UK = 18;
    public static final int LANGUAGE_ENGLISH_US = 0;
    public static final int LANGUAGE_JAPANESE = 16;
    public static final int LANGUAGE_JAPANESE_LINUX = 17;
    public static final String MOUSE_SYNC_CURSOR = "MouseSyncCursor";
    public static final String OPTIONS_BANDWIDTH_100MBPS = "OptionsBandwidth100Mbps";
    public static final String OPTIONS_BANDWIDTH_10MBPS = "OptionsBandwidth10Mbps";
    public static final String OPTIONS_BANDWIDTH_1MBPS = "OptionsBandwidth1Mbps";
    public static final String OPTIONS_BANDWIDTH_256KBPS = "OptionsBandwidth256Kbps";
    public static final String OPTIONS_BANDWIDTH_512KBPS = "OptionsBandwidth512Kbps";
    public static final String OPTIONS_BANDWIDTH_AUTO_DETECT = "OptionsBandwidthAutoDetect";
    public static final String OPTIONS_VIDEO_SETTINGS = "OptionsVideoSettings";
    public static final String SKBRD_LANGUAGE = "SkbrdLanguage";
    public static final String SKBRD_LANGUAGE_ENGLISH_UK = "English(United Kingdom)";
    public static final String SKBRD_LANGUAGE_ENGLISH_US = "English(United States)";
    public static final String SKBRD_LANGUAGE_JAPANESE = "Japanese";
    public static final String SKBRD_LANGUAGE_JAPANESE_LINUX = "Japanese(Linux-32Bit only)";
    public static final String VIDEO_COMPRESSION_NONE = "VideoCompressionNone";
    public static final String VIDEO_EXIT = "VideoExit";
    public static final String VIDEO_FULL_SCREEN = "VideoFullScreen";
    public static final String VIDEO_PAUSE_REDIRECTION = "VideoPauseRedirection";
    public static final String VIDEO_REFRESH = "VideoRefresh";
    public static final String VIDEO_RESTART = "VideoRestart";
    public static final String VIDEO_RESUME_REDIRECTION = "VideoResumeRedirection";
    public static final String VIDEO_START_REDIRECTION = "VideoStartRedirection";
    public static final String VIDEO_STOP_REDIRECTION = "VideoStopRedirection";
    public static ButtonGroup group;
    protected static JVMenuItemStatusListener m_itemStatus = new JVMenuItemStatusListener();
    protected static JVMenuListener m_menuListener = new JVMenuListener();
    protected static JVMenuStatusListener m_menuStatus = new JVMenuStatusListener();
    protected static boolean m_mouseSync = false;
    protected static String m_stateBandwidth = OPTIONS_BANDWIDTH_100MBPS;
    protected static boolean m_stateFullKeyboard = false;
    protected static boolean m_stateFullScr = false;
    protected static boolean m_stateKMCrypt = false;
    protected static boolean m_stateLAlt = false;
    protected static boolean m_stateLCtrl = false;
    protected static boolean m_stateLWinDown = false;
    protected static int m_stateLangIndex = 0;
    protected static boolean m_stateRAlt = false;
    protected static boolean m_stateRCtrl = false;
    protected static boolean m_stateRWinDown = false;
    protected static boolean m_stateRedir = false;
    protected static boolean m_stateRedirCDROM = false;
    protected static boolean m_stateRedirFloppy = false;
    protected static boolean m_stateRedirFloppyImage = false;
    protected static boolean m_stateRedirISO = false;
    protected static boolean m_stateSingleCursor = false;
    protected Hashtable<String, JMenuItem> m_menuItems = new Hashtable<>();

    public JMenuItem getMenuItem(String str) {
        return this.m_menuItems.get(str);
    }

    public boolean getRCtrlState() {
        return m_stateRCtrl;
    }

    public boolean getLCtrlState() {
        return m_stateLCtrl;
    }

    public boolean getLAltState() {
        return m_stateLAlt;
    }

    public boolean getRAltState() {
        return m_stateRAlt;
    }

    public void notifyRedirection(boolean z) {
        getMenuItem(VIDEO_PAUSE_REDIRECTION).setEnabled(z);
        getMenuItem(VIDEO_RESUME_REDIRECTION).setEnabled(!z);
        m_stateRedir = z;
    }

    public void notifyPauseRedirection(boolean z) {
        getMenuItem(VIDEO_PAUSE_REDIRECTION).setEnabled(z);
    }

    public void notifyFullScreen(boolean z) {
        getMenuItem(VIDEO_FULL_SCREEN).setSelected(z);
        m_stateFullScr = z;
    }

    public void notifyFullScreenEnable(boolean z) {
        getMenuItem(VIDEO_FULL_SCREEN).setEnabled(false);
    }

    public void notifyRCtrl(boolean z) {
        getMenuItem(KEYBOARD_RIGHT_CTRL_KEY).setSelected(z);
        m_stateRCtrl = z;
    }

    public void notifyLCtrl(boolean z) {
        getMenuItem(KEYBOARD_LEFT_CTRL_KEY).setSelected(z);
        m_stateLCtrl = z;
    }

    public void notifyRAlt(boolean z) {
        getMenuItem(KEYBOARD_RIGHT_ALT_KEY).setSelected(z);
        m_stateRAlt = z;
    }

    public void notifyLAlt(boolean z) {
        getMenuItem(KEYBOARD_LEFT_ALT_KEY).setSelected(z);
        m_stateLAlt = z;
    }

    public void notifyRWinDown(boolean z) {
        getMenuItem(KEYBOARD_RIGHT_WINKEY_PRESSHOLD).setSelected(z);
        m_stateRWinDown = z;
    }

    public void notifyLWinDown(boolean z) {
        getMenuItem(KEYBOARD_LEFT_WINKEY_PRESSHOLD).setSelected(z);
        m_stateLWinDown = z;
    }

    public void notifyBandwidth(String str) {
        getMenuItem(str).setSelected(true);
        m_stateBandwidth = str;
    }

    public void notifyCDROMRedir(boolean z) {
        getMenuItem(DEVICE_CDROM_REDIRECTION).setSelected(z);
        m_stateRedirCDROM = z;
    }

    public void notifyISORedir(boolean z) {
        getMenuItem(DEVICE_ISO_REDIRECTION).setSelected(z);
        m_stateRedirISO = z;
    }

    public void notifyFloppyRedir(boolean z) {
        getMenuItem(DEVICE_FLOPPY_REDIRECTION).setSelected(z);
        m_stateRedirFloppy = z;
    }

    public void notifyFloppyImageRedir(boolean z) {
        getMenuItem(DEVICE_FLOPPY_IMAGE_REDIRECTION).setSelected(z);
        m_stateRedirFloppyImage = z;
    }

    public void notifyMouseCursor(boolean z) {
        getMenuItem(MOUSE_SYNC_CURSOR).setSelected(z);
        m_mouseSync = z;
    }

    public void notifyFullKeyboard(boolean z) {
        getMenuItem(KEYBOARD_FULL_KEYBOARD).setSelected(z);
        m_stateFullKeyboard = z;
    }

    public void notifyMultKeyBoard(int i) {
        m_stateLangIndex = i;
        if (m_stateLangIndex == 0) {
            getMenuItem(SKBRD_LANGUAGE_ENGLISH_US).setSelected(true);
        } else if (m_stateLangIndex == 16) {
            getMenuItem(SKBRD_LANGUAGE_JAPANESE).setSelected(true);
        } else if (m_stateLangIndex == 17) {
            getMenuItem(SKBRD_LANGUAGE_JAPANESE_LINUX).setSelected(true);
        } else if (m_stateLangIndex == 18) {
            getMenuItem(SKBRD_LANGUAGE_ENGLISH_UK).setSelected(true);
        }
    }

    public void refreshMenu() {
        notifyRedirection(m_stateRedir);
        notifyFullScreen(m_stateFullScr);
        notifyBandwidth(m_stateBandwidth);
        notifyRCtrl(m_stateRCtrl);
        notifyLCtrl(m_stateLCtrl);
        notifyRAlt(m_stateRAlt);
        notifyLAlt(m_stateLAlt);
        notifyRWinDown(m_stateRWinDown);
        notifyLWinDown(m_stateLWinDown);
        notifyCDROMRedir(m_stateRedirCDROM);
        notifyISORedir(m_stateRedirISO);
        notifyFloppyRedir(m_stateRedirFloppy);
        notifyFloppyImageRedir(m_stateRedirFloppyImage);
        notifyMouseCursor(m_mouseSync);
        notifyFullKeyboard(m_stateFullKeyboard);
        notifyMultKeyBoard(m_stateLangIndex);
    }

    private static class JVMenuListener implements ActionListener {
        private JVMenuListener() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String actionCommand = actionEvent.getActionCommand();
            JViewerApp instance = JViewerApp.getInstance();
            if (actionCommand.equals(JVMenu.VIDEO_START_REDIRECTION)) {
                instance.OnVideoStartRedirection();
            } else if (actionCommand.equals(JVMenu.VIDEO_PAUSE_REDIRECTION)) {
                instance.OnVideoPauseRedirection();
            } else if (actionCommand.equals(JVMenu.VIDEO_RESUME_REDIRECTION)) {
                instance.OnVideoResumeRedirection();
            } else if (actionCommand.equals(JVMenu.VIDEO_REFRESH)) {
                instance.OnVideoRefreshRedirection();
            } else if (actionCommand.equals(JVMenu.VIDEO_FULL_SCREEN)) {
                instance.OnVideoFullScreen(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.VIDEO_EXIT)) {
                instance.OnVideoExit();
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_CTRL_KEY)) {
                instance.OnKeyboardHoldRightCtrlKey(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_ALT_KEY)) {
                instance.OnKeyboardHoldRightAltKey(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_CTRL_KEY)) {
                instance.OnKeyboardHoldLeftCtrlKey(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_ALT_KEY)) {
                instance.OnKeyboardHoldLeftAltKey(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_WINKEY_PRESSHOLD)) {
                instance.OnKeyboardLeftWindowsKeyHoldDown(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_WINKEY_PRESSRELEASE)) {
                instance.OnKeyboardLeftWindowsKeyPressRelease();
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_WINKEY_PRESSHOLD)) {
                instance.OnKeyboardRightWindowsKeyHoldDown(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_WINKEY_PRESSRELEASE)) {
                instance.OnKeyboardRightWindowsKeyPressRelease();
            } else if (actionCommand.equals(JVMenu.KEYBOARD_CTRL_ALT_DEL)) {
                instance.OnKeyboardAltCtrlDel();
            } else if (actionCommand.equals(JVMenu.KEYBOARD_FULL_KEYBOARD)) {
                instance.OnKeyboardFullKeyboard(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.MOUSE_SYNC_CURSOR)) {
                instance.OnUSBMouseSyncCursor(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_AUTO_DETECT)) {
                instance.OnOptionsBandwidthAutoDetect();
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_256KBPS)) {
                instance.OnOptionsBandwidth256Kbps();
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_512KBPS)) {
                instance.OnOptionsBandwidth512Kbps();
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_1MBPS)) {
                instance.OnOptionsBandwidth1Mbps();
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_10MBPS)) {
                instance.OnOptionsBandwidth10Mbps();
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_100MBPS)) {
                instance.OnOptionsBandwidth100Mbps();
            } else if (actionCommand.equals(JVMenu.OPTIONS_VIDEO_SETTINGS)) {
                instance.OnOptionsVideoSettings();
            } else if (actionCommand.equals(JVMenu.HELP_ABOUT_RCONSOLE)) {
                instance.OnHelpAboutJViewer();
            } else if (actionCommand.equals(JVMenu.DEVICE_CDROM_REDIRECTION)) {
                JVMenu.m_stateRedirCDROM = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
                instance.OnCDROMRedir(JVMenu.m_stateRedirCDROM);
            } else if (actionCommand.equals(JVMenu.DEVICE_ISO_REDIRECTION)) {
                JVMenu.m_stateRedirISO = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
                instance.OnISORedir(JVMenu.m_stateRedirISO);
            } else if (actionCommand.equals(JVMenu.DEVICE_FLOPPY_REDIRECTION)) {
                JVMenu.m_stateRedirFloppy = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
                instance.OnFloppyRedir(JVMenu.m_stateRedirFloppy);
            } else if (actionCommand.equals(JVMenu.DEVICE_FLOPPY_IMAGE_REDIRECTION)) {
                JVMenu.m_stateRedirFloppyImage = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
                instance.OnFloppyImageRedir(JVMenu.m_stateRedirFloppyImage);
            } else if (actionCommand.equals(JVMenu.SKBRD_LANGUAGE_ENGLISH_US)) {
                instance.OnSetLangIndex(0);
            } else if (actionCommand.equals(JVMenu.SKBRD_LANGUAGE_JAPANESE)) {
                instance.OnSetLangIndex(16);
            } else if (actionCommand.equals(JVMenu.SKBRD_LANGUAGE_JAPANESE_LINUX)) {
                instance.OnSetLangIndex(17);
            } else if (actionCommand.equals(JVMenu.SKBRD_LANGUAGE_ENGLISH_UK)) {
                instance.OnSetLangIndex(18);
            }
        }
    }

    private static class JVMenuStatusListener extends MouseAdapter {
        private JVMenuStatusListener() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            String text = ((JMenu) mouseEvent.getSource()).getText();
            JViewerApp instance = JViewerApp.getInstance();
            if (text.equals("Video")) {
                instance.setStatus("Video commands");
            } else if (text.equals("Keyboard")) {
                instance.setStatus("Keyboard commands");
            } else if (text.equals("Left Windows Key")) {
                instance.setStatus("Left windows key commands");
            } else if (text.equals("Right Windows Key")) {
                instance.setStatus("Right windows key commands");
            } else if (text.equals("Soft Keyboard")) {
                instance.setStatus("Soft keyboard selection");
            } else if (text.equals("Mouse")) {
                instance.setStatus("Mouse commands");
            } else if (text.equals("Mouse Icon")) {
                instance.setStatus("Mouse icon selection");
            } else if (text.equals("Options")) {
                instance.setStatus("Options setting");
            } else if (text.equals("Bandwidth")) {
                instance.setStatus("Bandwidth selection");
            } else if (text.equals("Quality")) {
                instance.setStatus("Quality selection");
            } else if (text.equals("Macros")) {
                instance.setStatus("Macros commands");
            } else if (text.equals("Help")) {
                instance.setStatus("About KVM Remote Console application");
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            JViewerApp.getInstance().resetStatus();
        }
    }

    private static class JVMenuItemStatusListener extends MouseAdapter {
        private JVMenuItemStatusListener() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            String actionCommand = ((JMenuItem) mouseEvent.getSource()).getActionCommand();
            JViewerApp instance = JViewerApp.getInstance();
            if (actionCommand.equals(JVMenu.VIDEO_START_REDIRECTION)) {
                instance.setStatus("Start video redirection");
            } else if (actionCommand.equals(JVMenu.VIDEO_PAUSE_REDIRECTION)) {
                instance.setStatus("Pause video redirection");
            } else if (actionCommand.equals(JVMenu.VIDEO_RESUME_REDIRECTION)) {
                instance.setStatus("Resume video redirection");
            } else if (actionCommand.equals(JVMenu.VIDEO_REFRESH)) {
                instance.setStatus("Refresh video");
            } else if (actionCommand.equals(JVMenu.VIDEO_FULL_SCREEN)) {
                instance.setStatus("Change to full screen or regular mode");
            } else if (actionCommand.equals(JVMenu.VIDEO_EXIT)) {
                instance.setStatus("Exit KVM remote console");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_CTRL_KEY)) {
                instance.setStatus("Hold down right control key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_ALT_KEY)) {
                instance.setStatus("Hold down right alt key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_CTRL_KEY)) {
                instance.setStatus("Hold down left control key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_ALT_KEY)) {
                instance.setStatus("Hold down left alt key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_WINKEY_PRESSHOLD)) {
                instance.setStatus("Hold down left windows key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_LEFT_WINKEY_PRESSRELEASE)) {
                instance.setStatus("Press and release left windows key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_WINKEY_PRESSHOLD)) {
                instance.setStatus("Hold down right windows key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_RIGHT_WINKEY_PRESSRELEASE)) {
                instance.setStatus("Press and release right windows key");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_CTRL_ALT_DEL)) {
                instance.setStatus("Press Alt + Ctrl + Del key combination");
            } else if (actionCommand.equals(JVMenu.KEYBOARD_FULL_KEYBOARD)) {
                instance.setStatus("Enable/disable full keyboard");
            } else if (actionCommand.equals(JVMenu.MOUSE_SYNC_CURSOR)) {
                instance.setStatus("Synchronize mouse cursor");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_AUTO_DETECT)) {
                instance.setStatus("Auto detect bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_256KBPS)) {
                instance.setStatus("Select 256 Kbps bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_512KBPS)) {
                instance.setStatus("Select 512 Kbps bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_1MBPS)) {
                instance.setStatus("Select 1 Mbps bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_10MBPS)) {
                instance.setStatus("Select 10 Mbps bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_BANDWIDTH_100MBPS)) {
                instance.setStatus("Select 100 Mbps bandwith");
            } else if (actionCommand.equals(JVMenu.OPTIONS_VIDEO_SETTINGS)) {
                instance.setStatus("Video settings, color gains and screen position");
            } else if (actionCommand.equals(JVMenu.HELP_ABOUT_RCONSOLE)) {
                instance.setStatus("About KVM Remote Console application");
            } else if (actionCommand.equals(JVMenu.DEVICE_CDROM_REDIRECTION)) {
                instance.setStatus("CDROM Redirection");
            } else if (actionCommand.equals(JVMenu.DEVICE_ISO_REDIRECTION)) {
                instance.setStatus("ISO9660 Image Redirection");
            } else if (actionCommand.equals(JVMenu.DEVICE_FLOPPY_REDIRECTION)) {
                instance.setStatus("Floppy Redirection");
            } else if (actionCommand.equals(JVMenu.DEVICE_FLOPPY_IMAGE_REDIRECTION)) {
                instance.setStatus("Floppy Image Redirection");
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            JViewerApp.getInstance().resetStatus();
        }
    }

    /* access modifiers changed from: protected */
    public JMenu constructVideoMenu() {
        JMenu jMenu = new JMenu("Video");
        jMenu.addMouseListener(m_menuStatus);
        jMenu.setMnemonic('V');
        JMenuItem jMenuItem = new JMenuItem("Start Redirection");
        jMenuItem.addActionListener(m_menuListener);
        jMenuItem.addMouseListener(m_itemStatus);
        jMenuItem.setMnemonic('S');
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, 8));
        jMenuItem.setActionCommand(VIDEO_START_REDIRECTION);
        this.m_menuItems.put(VIDEO_START_REDIRECTION, jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Pause Redirection");
        jMenuItem2.addActionListener(m_menuListener);
        jMenuItem2.addMouseListener(m_itemStatus);
        jMenuItem2.setMnemonic('P');
        jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(80, 8));
        jMenuItem2.setActionCommand(VIDEO_PAUSE_REDIRECTION);
        jMenu.add(jMenuItem2);
        this.m_menuItems.put(VIDEO_PAUSE_REDIRECTION, jMenuItem2);
        JMenuItem jMenuItem3 = new JMenuItem("Resume Redirection");
        jMenuItem3.addActionListener(m_menuListener);
        jMenuItem3.addMouseListener(m_itemStatus);
        jMenuItem3.setMnemonic('R');
        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke(82, 8));
        jMenuItem3.setActionCommand(VIDEO_RESUME_REDIRECTION);
        jMenu.add(jMenuItem3);
        this.m_menuItems.put(VIDEO_RESUME_REDIRECTION, jMenuItem3);
        JMenuItem jMenuItem4 = new JMenuItem("Refresh Video");
        jMenuItem4.addActionListener(m_menuListener);
        jMenuItem4.addMouseListener(m_itemStatus);
        jMenuItem4.setActionCommand(VIDEO_REFRESH);
        jMenu.add(jMenuItem4);
        this.m_menuItems.put(VIDEO_REFRESH, jMenuItem4);
        jMenu.addSeparator();
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Full Screen");
        jCheckBoxMenuItem.addActionListener(m_menuListener);
        jCheckBoxMenuItem.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem.setActionCommand(VIDEO_FULL_SCREEN);
        jCheckBoxMenuItem.setMnemonic('F');
        jCheckBoxMenuItem.setAccelerator(KeyStroke.getKeyStroke(70, 8));
        jMenu.add(jCheckBoxMenuItem);
        this.m_menuItems.put(VIDEO_FULL_SCREEN, jCheckBoxMenuItem);
        jMenu.addSeparator();
        JMenuItem jMenuItem5 = new JMenuItem("Exit");
        jMenuItem5.addActionListener(m_menuListener);
        jMenuItem5.addMouseListener(m_itemStatus);
        jMenuItem5.setActionCommand(VIDEO_EXIT);
        jMenu.add(jMenuItem5);
        this.m_menuItems.put(VIDEO_EXIT, jMenuItem5);
        return jMenu;
    }

    /* access modifiers changed from: protected */
    public JMenu constructKeyboardMenu() {
        JMenu jMenu = new JMenu("Keyboard");
        jMenu.addMouseListener(m_menuStatus);
        jMenu.setMnemonic('K');
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Hold Right Ctrl Key");
        jCheckBoxMenuItem.addActionListener(m_menuListener);
        jCheckBoxMenuItem.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem.setMnemonic('C');
        jCheckBoxMenuItem.setActionCommand(KEYBOARD_RIGHT_CTRL_KEY);
        jMenu.add(jCheckBoxMenuItem);
        this.m_menuItems.put(KEYBOARD_RIGHT_CTRL_KEY, jCheckBoxMenuItem);
        JMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Hold Right Alt Key");
        jCheckBoxMenuItem2.addActionListener(m_menuListener);
        jCheckBoxMenuItem2.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem2.setMnemonic('A');
        jCheckBoxMenuItem2.setActionCommand(KEYBOARD_RIGHT_ALT_KEY);
        jMenu.add(jCheckBoxMenuItem2);
        this.m_menuItems.put(KEYBOARD_RIGHT_ALT_KEY, jCheckBoxMenuItem2);
        JMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Hold Left Ctrl Key");
        jCheckBoxMenuItem3.addActionListener(m_menuListener);
        jCheckBoxMenuItem3.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem3.setMnemonic('t');
        jCheckBoxMenuItem3.setDisplayedMnemonicIndex(11);
        jCheckBoxMenuItem3.setActionCommand(KEYBOARD_LEFT_CTRL_KEY);
        jMenu.add(jCheckBoxMenuItem3);
        this.m_menuItems.put(KEYBOARD_LEFT_CTRL_KEY, jCheckBoxMenuItem3);
        JMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem("Hold Left Alt Key");
        jCheckBoxMenuItem4.addActionListener(m_menuListener);
        jCheckBoxMenuItem4.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem4.setMnemonic('l');
        jCheckBoxMenuItem4.setDisplayedMnemonicIndex(11);
        jCheckBoxMenuItem4.setActionCommand(KEYBOARD_LEFT_ALT_KEY);
        jMenu.add(jCheckBoxMenuItem4);
        this.m_menuItems.put(KEYBOARD_LEFT_ALT_KEY, jCheckBoxMenuItem4);
        jMenu.addSeparator();
        JMenu jMenu2 = new JMenu("Left Windows Key");
        jMenu2.addMouseListener(m_menuStatus);
        JMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem("Hold Down");
        jCheckBoxMenuItem5.addActionListener(m_menuListener);
        jCheckBoxMenuItem5.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem5.setActionCommand(KEYBOARD_LEFT_WINKEY_PRESSHOLD);
        jMenu2.add(jCheckBoxMenuItem5);
        this.m_menuItems.put(KEYBOARD_LEFT_WINKEY_PRESSHOLD, jCheckBoxMenuItem5);
        JMenuItem jMenuItem = new JMenuItem("Press and Release");
        jMenuItem.addActionListener(m_menuListener);
        jMenuItem.addMouseListener(m_itemStatus);
        jMenuItem.setActionCommand(KEYBOARD_LEFT_WINKEY_PRESSRELEASE);
        jMenu2.add(jMenuItem);
        this.m_menuItems.put(KEYBOARD_LEFT_WINKEY_PRESSRELEASE, jMenuItem);
        jMenu.add(jMenu2);
        JMenu jMenu3 = new JMenu("Right Windows Key");
        jMenu3.addMouseListener(m_menuStatus);
        JMenuItem jCheckBoxMenuItem6 = new JCheckBoxMenuItem("Hold Down");
        jCheckBoxMenuItem6.addActionListener(m_menuListener);
        jCheckBoxMenuItem6.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem6.setActionCommand(KEYBOARD_RIGHT_WINKEY_PRESSHOLD);
        jMenu3.add(jCheckBoxMenuItem6);
        this.m_menuItems.put(KEYBOARD_RIGHT_WINKEY_PRESSHOLD, jCheckBoxMenuItem6);
        JMenuItem jMenuItem2 = new JMenuItem("Press and Release");
        jMenuItem2.addActionListener(m_menuListener);
        jMenuItem2.addMouseListener(m_itemStatus);
        jMenuItem2.setActionCommand(KEYBOARD_RIGHT_WINKEY_PRESSRELEASE);
        jMenu3.add(jMenuItem2);
        this.m_menuItems.put(KEYBOARD_RIGHT_WINKEY_PRESSRELEASE, jMenuItem2);
        jMenu.add(jMenu3);
        JMenuItem jMenuItem3 = new JMenuItem("Alt+Ctrl+Del");
        jMenuItem3.addActionListener(m_menuListener);
        jMenuItem3.addMouseListener(m_itemStatus);
        jMenuItem3.setActionCommand(KEYBOARD_CTRL_ALT_DEL);
        jMenu.add(jMenuItem3);
        this.m_menuItems.put(KEYBOARD_CTRL_ALT_DEL, jMenuItem3);
        jMenu.addSeparator();
        JMenuItem jCheckBoxMenuItem7 = new JCheckBoxMenuItem("Full Keyboard");
        jCheckBoxMenuItem7.addActionListener(m_menuListener);
        jCheckBoxMenuItem7.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem7.setActionCommand(KEYBOARD_FULL_KEYBOARD);
        jMenu.add(jCheckBoxMenuItem7);
        this.m_menuItems.put(KEYBOARD_FULL_KEYBOARD, jCheckBoxMenuItem7);
        jCheckBoxMenuItem7.setEnabled(true);
        jMenu.addSeparator();
        JMenu jMenu4 = new JMenu("Languages");
        jMenu4.addActionListener(m_menuListener);
        jMenu4.addMouseListener(m_menuStatus);
        group = new ButtonGroup();
        JMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(SKBRD_LANGUAGE_ENGLISH_US);
        jRadioButtonMenuItem.addActionListener(m_menuListener);
        jRadioButtonMenuItem.setActionCommand(SKBRD_LANGUAGE_ENGLISH_US);
        group.add(jRadioButtonMenuItem);
        jMenu4.add(jRadioButtonMenuItem);
        this.m_menuItems.put(SKBRD_LANGUAGE_ENGLISH_US, jRadioButtonMenuItem);
        jRadioButtonMenuItem.setEnabled(true);
        JMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem(SKBRD_LANGUAGE_JAPANESE);
        jRadioButtonMenuItem2.addActionListener(m_menuListener);
        jRadioButtonMenuItem2.setActionCommand(SKBRD_LANGUAGE_JAPANESE);
        group.add(jRadioButtonMenuItem2);
        jMenu4.add(jRadioButtonMenuItem2);
        this.m_menuItems.put(SKBRD_LANGUAGE_JAPANESE, jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setEnabled(true);
        JMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem(SKBRD_LANGUAGE_ENGLISH_UK);
        jRadioButtonMenuItem3.addActionListener(m_menuListener);
        jRadioButtonMenuItem3.setActionCommand(SKBRD_LANGUAGE_ENGLISH_UK);
        group.add(jRadioButtonMenuItem3);
        jMenu4.add(jRadioButtonMenuItem3);
        this.m_menuItems.put(SKBRD_LANGUAGE_ENGLISH_UK, jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setEnabled(true);
        jMenu.add(jMenu4);
        return jMenu;
    }

    /* access modifiers changed from: protected */
    public JMenu constructMouseMenu() {
        JMenu jMenu = new JMenu("Mouse");
        jMenu.addMouseListener(m_menuStatus);
        jMenu.setMnemonic('E');
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Sync Cursor");
        jCheckBoxMenuItem.addActionListener(m_menuListener);
        jCheckBoxMenuItem.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem.setAccelerator(KeyStroke.getKeyStroke(77, 8));
        jCheckBoxMenuItem.setActionCommand(MOUSE_SYNC_CURSOR);
        jMenu.add(jCheckBoxMenuItem);
        this.m_menuItems.put(MOUSE_SYNC_CURSOR, jCheckBoxMenuItem);
        return jMenu;
    }

    /* access modifiers changed from: protected */
    public JMenu constructOptionsMenu() {
        JMenu jMenu = new JMenu("Options");
        jMenu.addMouseListener(m_menuStatus);
        jMenu.setMnemonic('O');
        JMenu jMenu2 = new JMenu("Bandwidth");
        jMenu2.addMouseListener(m_menuStatus);
        JMenuItem jMenuItem = new JMenuItem("Auto Detect");
        jMenuItem.addActionListener(m_menuListener);
        jMenuItem.addMouseListener(m_itemStatus);
        jMenuItem.setActionCommand(OPTIONS_BANDWIDTH_AUTO_DETECT);
        jMenu2.add(jMenuItem);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_AUTO_DETECT, jMenuItem);
        jMenu2.addSeparator();
        ButtonGroup buttonGroup = new ButtonGroup();
        JMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem("256 Kbps");
        jRadioButtonMenuItem.addActionListener(m_menuListener);
        jRadioButtonMenuItem.addMouseListener(m_itemStatus);
        jRadioButtonMenuItem.setActionCommand(OPTIONS_BANDWIDTH_256KBPS);
        buttonGroup.add(jRadioButtonMenuItem);
        jMenu2.add(jRadioButtonMenuItem);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_256KBPS, jRadioButtonMenuItem);
        JMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("512 Kbps");
        jRadioButtonMenuItem2.addActionListener(m_menuListener);
        jRadioButtonMenuItem2.addMouseListener(m_itemStatus);
        jRadioButtonMenuItem2.setActionCommand(OPTIONS_BANDWIDTH_512KBPS);
        buttonGroup.add(jRadioButtonMenuItem2);
        jMenu2.add(jRadioButtonMenuItem2);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_512KBPS, jRadioButtonMenuItem2);
        JMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("1 Mbps");
        jRadioButtonMenuItem3.addActionListener(m_menuListener);
        jRadioButtonMenuItem3.addMouseListener(m_itemStatus);
        jRadioButtonMenuItem3.setActionCommand(OPTIONS_BANDWIDTH_1MBPS);
        buttonGroup.add(jRadioButtonMenuItem3);
        jMenu2.add(jRadioButtonMenuItem3);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_1MBPS, jRadioButtonMenuItem3);
        JMenuItem jRadioButtonMenuItem4 = new JRadioButtonMenuItem("10 Mbps");
        jRadioButtonMenuItem4.addActionListener(m_menuListener);
        jRadioButtonMenuItem4.addMouseListener(m_itemStatus);
        jRadioButtonMenuItem4.setActionCommand(OPTIONS_BANDWIDTH_10MBPS);
        buttonGroup.add(jRadioButtonMenuItem4);
        jMenu2.add(jRadioButtonMenuItem4);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_10MBPS, jRadioButtonMenuItem4);
        JMenuItem jRadioButtonMenuItem5 = new JRadioButtonMenuItem("100 Mbps");
        jRadioButtonMenuItem5.addActionListener(m_menuListener);
        jRadioButtonMenuItem5.addMouseListener(m_itemStatus);
        jRadioButtonMenuItem5.setActionCommand(OPTIONS_BANDWIDTH_100MBPS);
        buttonGroup.add(jRadioButtonMenuItem5);
        jMenu2.add(jRadioButtonMenuItem5);
        this.m_menuItems.put(OPTIONS_BANDWIDTH_100MBPS, jRadioButtonMenuItem5);
        jMenu.add(jMenu2);
        JMenuItem jMenuItem2 = new JMenuItem("Video Settings...");
        jMenuItem2.addActionListener(m_menuListener);
        jMenuItem2.addMouseListener(m_itemStatus);
        jMenuItem2.setActionCommand(OPTIONS_VIDEO_SETTINGS);
        jMenu.add(jMenuItem2);
        this.m_menuItems.put(OPTIONS_VIDEO_SETTINGS, jMenuItem2);
        return jMenu;
    }

    /* access modifiers changed from: protected */
    public JMenu constructDeviceRedirMenu() {
        JMenu jMenu = new JMenu("Device");
        jMenu.addMouseListener(m_menuStatus);
        jMenu.setMnemonic('D');
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Redirect CDROM");
        jCheckBoxMenuItem.addActionListener(m_menuListener);
        jCheckBoxMenuItem.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem.setMnemonic('C');
        jCheckBoxMenuItem.setActionCommand(DEVICE_CDROM_REDIRECTION);
        jMenu.add(jCheckBoxMenuItem);
        this.m_menuItems.put(DEVICE_CDROM_REDIRECTION, jCheckBoxMenuItem);
        JMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Redirect ISO");
        jCheckBoxMenuItem2.addActionListener(m_menuListener);
        jCheckBoxMenuItem2.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem2.setMnemonic('I');
        jCheckBoxMenuItem2.setActionCommand(DEVICE_ISO_REDIRECTION);
        jMenu.add(jCheckBoxMenuItem2);
        this.m_menuItems.put(DEVICE_ISO_REDIRECTION, jCheckBoxMenuItem2);
        JMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Redirect Floppy / USB");
        jCheckBoxMenuItem3.addActionListener(m_menuListener);
        jCheckBoxMenuItem3.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem3.setMnemonic('F');
        jCheckBoxMenuItem3.setActionCommand(DEVICE_FLOPPY_REDIRECTION);
        jMenu.add(jCheckBoxMenuItem3);
        this.m_menuItems.put(DEVICE_FLOPPY_REDIRECTION, jCheckBoxMenuItem3);
        JMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem("Redirect Floppy Image");
        jCheckBoxMenuItem4.addActionListener(m_menuListener);
        jCheckBoxMenuItem4.addMouseListener(m_itemStatus);
        jCheckBoxMenuItem4.setMnemonic('l');
        jCheckBoxMenuItem4.setActionCommand(DEVICE_FLOPPY_IMAGE_REDIRECTION);
        jMenu.add(jCheckBoxMenuItem4);
        this.m_menuItems.put(DEVICE_FLOPPY_IMAGE_REDIRECTION, jCheckBoxMenuItem4);
        return jMenu;
    }

    /* access modifiers changed from: protected */
    public JMenu constructHelpMenu() {
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        jMenu.addMouseListener(m_menuStatus);
        JMenuItem jMenuItem = new JMenuItem("About JViewer...");
        jMenuItem.addActionListener(m_menuListener);
        jMenuItem.addMouseListener(m_itemStatus);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(112, 8));
        jMenuItem.setActionCommand(HELP_ABOUT_RCONSOLE);
        jMenu.add(jMenuItem);
        this.m_menuItems.put(HELP_ABOUT_RCONSOLE, jMenuItem);
        return jMenu;
    }

    class GridMenu extends JMenu {
        GridMenu(String str, int i, int i2) {
            super(str);
            getPopupMenu().setLayout(new GridLayout(i, i2));
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public void setPopupMenuVisible(boolean z) {
            if (z == isPopupMenuVisible()) {
                return;
            }
            if (!z || !isShowing()) {
                getPopupMenu().setVisible(false);
                return;
            }
            getPopupMenu().show(this, getWidth(), 0);
        }
    }
}
