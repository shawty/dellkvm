package com.ami.kvm.jviewer.gui;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.hid.KMEncode;
import com.ami.kvm.jviewer.hid.KVMClient;
import com.ami.kvm.jviewer.hid.PS2Encode;
import com.ami.kvm.jviewer.kvmpkts.PS2KeyboardRep;
import com.ami.kvm.jviewer.kvmpkts.PS2MouseRep;
import com.ami.kvm.jviewer.kvmpkts.USBKeyboardRep;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import com.ami.kvm.jviewer.kvmpkts.USBMouseRep;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class JViewerView extends JComponent {
    public static int ACCEL_THRESHOLD = 4;
    public static final int DEFAULT_VIEW_HEIGHT = 1200;
    public static final int DEFAULT_VIEW_WIDTH = 1600;
    public static int MOUSE_ACCELERATION = 2;
    private static final int REL_MOUSE_MIN_WND_HEIGHT = 200;
    private static final int REL_MOUSE_MIN_WND_WIDTH = 200;
    public static boolean m_isFullKybrd = false;
    public static final boolean m_isG4 = true;
    private int lastX = -1;
    private int lastY = -1;
    private USBKeyboardRep m_USBKeyRep;
    private byte m_USBMouseMode = USBMouseRep.INVALID_MOUSE_MODE;
    private USBMouseRep m_USBMouseRep;
    private int m_act_height;
    private int m_act_width;
    private boolean m_bFirstInit = true;
    private boolean m_bLeftCtrlDown = false;
    private boolean m_bMouseRedirRunning = false;
    private boolean m_bMouseWasOut = true;
    private boolean m_bRightCtrlDown = false;
    private int m_height;
    private BufferedImage m_image;
    private KMEncode m_keyCoder;
    RCKeyListener m_keyListener;
    private PS2KeyboardRep m_keyRep;
    RCMouseListener m_mouseListener;
    private int m_mousePrevX = 0;
    private int m_mousePrevY = 0;
    private PS2MouseRep m_mouseRep;
    private int m_width;
    private Robot robbie;
    private int robotDX;
    private int robotDY;

    public JViewerView(int i, int i2) {
        setRCView(i, i2);
        this.m_USBKeyRep = new USBKeyboardRep();
        this.m_USBMouseRep = new USBMouseRep();
        this.m_mouseRep = new PS2MouseRep();
        this.m_keyRep = new PS2KeyboardRep();
        this.m_keyCoder = new PS2Encode();
        this.m_mouseListener = new RCMouseListener();
        this.m_keyListener = new RCKeyListener();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addFocusListener(new RCFocusListener());
    }

    public void addKMListeners() {
        addKeyListener(this.m_keyListener);
    }

    public void removeKMListeners() {
        removeKeyListener(this.m_keyListener);
        removeMouseListener(this.m_mouseListener);
        removeMouseMotionListener(this.m_mouseListener);
    }

    public short[] imageMemory() {
        return this.m_image.getRaster().getDataBuffer().getData();
    }

    public byte[] imageMemoryByte() {
        return this.m_image.getRaster().getDataBuffer().getData();
    }

    public int viewWidth() {
        return this.m_width;
    }

    public int viewHeight() {
        return this.m_height;
    }

    public void setRCView(int i, int i2) {
        this.m_width = i;
        this.m_act_width = i;
        this.m_height = i2;
        this.m_act_height = i2;
        setSize(i, i2);
        setPreferredSize(new Dimension(i, i2));
        setMaximumSize(new Dimension(i, i2));
        prepareBufImage();
        setAutoscrolls(true);
    }

    public void update(Graphics graphics) {
        paint(graphics);
    }

    public void paint(Graphics graphics) {
        try {
            Rectangle intersection = new Rectangle(0, 0, this.m_width, this.m_height).intersection(graphics.getClipBounds());
            graphics.drawImage(this.m_image.getSubimage(intersection.x, intersection.y, intersection.width, intersection.height), intersection.x, intersection.y, (ImageObserver) null);
        } catch (Exception e) {
        }
    }

    public void ShowCursor(boolean z) {
        if (z) {
            setCursor(null);
            return;
        }
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(32, 32, 3), new Point(0, 0), "Invisible Cursor"));
    }

    public void SetUSBMouseMode(byte b) {
        this.m_USBMouseMode = b;
    }

    public void SetVideoEngineConfigMode(byte b) {
        this.m_USBMouseMode = b;
        if (this.m_USBMouseMode == USBMouseRep.RELATIVE_MOUSE_MODE) {
            try {
                this.robbie = new Robot();
            } catch (AWTException e) {
                System.err.println("Cannot create robot for mouse repositioning");
                this.robbie = null;
            }
        }
    }

    public byte GetUSBMouseMode() {
        return this.m_USBMouseMode;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void mouse_resync_linux_mode(int i, int i2) {
        this.m_mouseListener.splitandsend(-this.m_width, -this.m_height, true);
        Point location = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(location, this);
        this.lastX = (int) location.getX();
        this.lastY = (int) location.getY();
        this.m_mouseListener.splitandsend(this.lastX, this.lastY, true);
    }

    public boolean USBsyncCursor(boolean z) {
        if (!z) {
            removeMouseListener(this.m_mouseListener);
            removeMouseMotionListener(this.m_mouseListener);
            this.m_bMouseRedirRunning = false;
            this.m_bMouseWasOut = true;
            return true;
        } else if (this.m_bMouseRedirRunning) {
            return true;
        } else {
            if (this.m_USBMouseMode == USBMouseRep.INVALID_MOUSE_MODE) {
                JViewerApp.getInstance().getMainWindow().generalErrorMessage("Sync Cursor", "Invalid Mouse Mode. Mouse Mode Response packet\nnot yet received from Video Server.\nPlease try after some time.");
                return false;
            }
            if (this.m_USBMouseMode == USBMouseRep.RELATIVE_MOUSE_MODE) {
                this.m_bMouseWasOut = true;
            }
            addMouseListener(this.m_mouseListener);
            addMouseMotionListener(this.m_mouseListener);
            this.m_bMouseRedirRunning = true;
            return true;
        }
    }

    public void syncCursor() {
        KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
        if (this.m_bFirstInit) {
            this.m_USBKeyRep.set(16, 2, true);
            kVMClient.sendKMMessage(this.m_USBKeyRep);
            this.m_USBKeyRep.set(16, 2, false);
            kVMClient.sendKMMessage(this.m_USBKeyRep);
            this.m_USBKeyRep.set(16, 2, true);
            kVMClient.sendKMMessage(this.m_USBKeyRep);
            this.m_USBKeyRep.set(16, 2, false);
            kVMClient.sendKMMessage(this.m_USBKeyRep);
            this.m_bFirstInit = false;
        }
    }

    private void prepareBufImage() {
        this.m_image = new BufferedImage(this.m_width, this.m_height, 5);
    }

    /* access modifiers changed from: package-private */
    public class RCKeyListener extends KeyAdapter {
        byte[] n_keys = new byte[4];

        RCKeyListener() {
        }

        public void keyPressed(KeyEvent keyEvent) {
            KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
            JViewerView.this.m_USBKeyRep = new USBKeyboardRep();
            JViewerApp.getInstance();
            int langIndex = JViewerApp.getLangIndex();
            if (!JViewerApp.getInstance().getJVMenu().getLCtrlState() || !JViewerApp.getInstance().getJVMenu().getRCtrlState() || keyEvent.getKeyCode() != 127) {
                if ((keyEvent.getModifiersEx() & 512) == 512 && !JViewerView.m_isFullKybrd) {
                    return;
                }
                if ((keyEvent.getModifiersEx() & USBMessage.IUSB_FROM_REMOTE) != 128 || JViewerView.m_isFullKybrd) {
                    if (keyEvent.getKeyCode() == 0 && keyEvent.getKeyLocation() == 0 && keyEvent.getKeyChar() == '/') {
                        JViewerView.this.m_USBKeyRep.set(47, 0, true);
                    } else if (keyEvent.getKeyCode() == 0 && keyEvent.getKeyLocation() == 0 && keyEvent.getKeyChar() == '\\') {
                        JViewerView.this.m_USBKeyRep.set(92, 0, true);
                    } else if (keyEvent.getKeyCode() == 0 && keyEvent.getKeyLocation() == 0 && keyEvent.getKeyChar() == '_') {
                        JViewerView.this.m_USBKeyRep.set(523, 0, true);
                    } else if (keyEvent.getKeyCode() == 0 && keyEvent.getKeyLocation() == 0 && keyEvent.getKeyChar() == '|') {
                        JViewerView.this.m_USBKeyRep.set(92, 16, true);
                    } else if (langIndex == 16 && keyEvent.getKeyChar() == '_') {
                        JViewerView.this.m_USBKeyRep.set(523, 1, true);
                    } else if (langIndex == 18 && keyEvent.getKeyCode() == 520) {
                        JViewerView.this.m_USBKeyRep.set(92, 1, true);
                    } else {
                        JViewerView.this.m_USBKeyRep.set(keyEvent.getKeyCode(), keyEvent.getKeyLocation(), true);
                    }
                    kVMClient.sendKMMessage(JViewerView.this.m_USBKeyRep);
                    if (langIndex == 16 && keyEvent.getKeyCode() == 525) {
                        try {
                            JViewerView.this.robbie = new Robot();
                            JViewerView.this.robbie.keyPress(16);
                            JViewerView.this.robbie.keyPress(121);
                        } catch (AWTException e) {
                            System.err.println("Cannot create robot for function (mouse right click)");
                        }
                    }
                    if (!JViewerView.m_isFullKybrd || (keyEvent.getKeyCode() != 512 && keyEvent.getKeyCode() != 77)) {
                        keyEvent.consume();
                    }
                }
            }
        }

        public void keyReleased(KeyEvent keyEvent) {
            KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
            JViewerView.this.m_USBKeyRep = new USBKeyboardRep();
            JViewerApp.getInstance();
            int langIndex = JViewerApp.getLangIndex();
            if ((keyEvent.getModifiersEx() & 512) == 512 && !JViewerView.m_isFullKybrd) {
                return;
            }
            if ((keyEvent.getModifiersEx() & USBMessage.IUSB_FROM_REMOTE) != 128 || JViewerView.m_isFullKybrd) {
                if (keyEvent.getKeyCode() == 244 || keyEvent.getKeyCode() == 243 || keyEvent.getKeyCode() == 240) {
                    if (JViewerView.m_isFullKybrd) {
                        JViewerView.this.m_USBKeyRep.set(keyEvent.getKeyCode(), keyEvent.getKeyLocation(), true);
                        kVMClient.sendKMMessage(JViewerView.this.m_USBKeyRep);
                    } else {
                        return;
                    }
                }
                if (langIndex == 18 && keyEvent.getKeyCode() == 520) {
                    JViewerView.this.m_USBKeyRep.set(92, keyEvent.getKeyLocation(), false);
                } else {
                    JViewerView.this.m_USBKeyRep.set(keyEvent.getKeyCode(), keyEvent.getKeyLocation(), false);
                }
                kVMClient.sendKMMessage(JViewerView.this.m_USBKeyRep);
                if (!JViewerView.m_isFullKybrd || (keyEvent.getKeyCode() != 512 && keyEvent.getKeyCode() != 77)) {
                    keyEvent.consume();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class RCMouseListener extends MouseInputAdapter {
        public static final byte LBUTTON_DOWN = 1;
        public static final byte MBUTTON_DOWN = 4;
        public static final byte MOUSE_MOVE = 0;
        public static final byte RBUTTON_DOWN = 2;
        private byte m_btnStatus = 0;

        RCMouseListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            int i;
            mouseEvent.getX();
            mouseEvent.getY();
            int x = JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE ? mouseEvent.getX() : 0;
            if (JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE) {
                i = mouseEvent.getY();
            } else {
                i = 0;
            }
            switch (mouseEvent.getButton()) {
                case 1:
                    this.m_btnStatus = (byte) (this.m_btnStatus | 1);
                    break;
                case 2:
                    this.m_btnStatus = (byte) (this.m_btnStatus | 4);
                    break;
                case 3:
                    this.m_btnStatus = (byte) (this.m_btnStatus | 2);
                    break;
                default:
                    return;
            }
            KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
            JViewerView.this.m_USBMouseRep.set(this.m_btnStatus, x, i, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
            kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            int i;
            mouseEvent.getX();
            mouseEvent.getY();
            int x = JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE ? mouseEvent.getX() : 0;
            if (JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE) {
                i = mouseEvent.getY();
            } else {
                i = 0;
            }
            switch (mouseEvent.getButton()) {
                case 1:
                    this.m_btnStatus = (byte) (this.m_btnStatus & -2);
                    break;
                case 2:
                    this.m_btnStatus = (byte) (this.m_btnStatus & -5);
                    break;
                case 3:
                    this.m_btnStatus = (byte) (this.m_btnStatus & -3);
                    break;
                default:
                    return;
            }
            KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
            JViewerView.this.m_USBMouseRep.set(this.m_btnStatus, x, i, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
            kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
        }

        private Point calculateCenter() {
            Dimension size = JViewerView.this.getTopLevelAncestor().getSize();
            return new Point(size.width / 2, size.height / 2);
        }

        public void mouseMoved(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE) {
                if (x < JViewerView.this.m_act_width && y < JViewerView.this.m_act_height) {
                    KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
                    JViewerView.this.m_USBMouseRep.set((byte) 0, x, y, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
                    kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
                }
            } else if (JViewerView.this.m_USBMouseMode == USBMouseRep.RELATIVE_MOUSE_MODE) {
                if (x >= JViewerView.this.m_act_width || y >= JViewerView.this.m_act_height) {
                    JViewerView.this.m_bMouseWasOut = true;
                } else if (JViewerView.this.m_bMouseWasOut) {
                    JViewerView.this.mouse_resync_linux_mode(x, y);
                    JViewerView.this.m_bMouseWasOut = false;
                } else {
                    splitandsend(x - JViewerView.this.lastX, y - JViewerView.this.lastY, true);
                    JViewerView.this.lastX = x;
                    JViewerView.this.lastY = y;
                }
            } else if (x < JViewerView.this.m_act_width && y < JViewerView.this.m_act_height) {
                splitandsend(x - JViewerView.this.lastX, y - JViewerView.this.lastY, false);
                JViewerView.this.lastX = x;
                JViewerView.this.lastY = y;
            }
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (JViewerView.this.m_USBMouseMode == USBMouseRep.ABSOLUTE_MOUSE_MODE) {
                if (x < JViewerView.this.m_act_width && y < JViewerView.this.m_act_height) {
                    KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
                    JViewerView.this.m_USBMouseRep.set(this.m_btnStatus, x, y, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
                    kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
                }
            } else if (JViewerView.this.m_USBMouseMode == USBMouseRep.RELATIVE_MOUSE_MODE) {
                if (x >= JViewerView.this.m_act_width || y >= JViewerView.this.m_act_height) {
                    JViewerView.this.m_bMouseWasOut = true;
                } else if (JViewerView.this.m_bMouseWasOut) {
                    JViewerView.this.mouse_resync_linux_mode(x, y);
                    JViewerView.this.m_bMouseWasOut = false;
                } else {
                    splitandsend(x - JViewerView.this.lastX, y - JViewerView.this.lastY, true);
                    JViewerView.this.lastX = x;
                    JViewerView.this.lastY = y;
                }
            } else if (x < JViewerView.this.m_act_width && y < JViewerView.this.m_act_height) {
                splitandsend(x - JViewerView.this.lastX, y - JViewerView.this.lastY, false);
                JViewerView.this.lastX = x;
                JViewerView.this.lastY = y;
            }
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
            JViewerView.this.m_bMouseWasOut = true;
        }

        public void splitandsend(int i, int i2, boolean z) {
            int i3;
            int i4;
            Debug.out.println("splitandsend: " + i + ", " + i2);
            int i5 = i >= 0 ? 1 : -1;
            int i6 = i2 >= 0 ? 1 : -1;
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            while (true) {
                if (abs > 126) {
                    i3 = 126;
                } else {
                    i3 = abs;
                }
                if (abs2 > 126) {
                    i4 = 126;
                } else {
                    i4 = abs2;
                }
                int i7 = abs - i3;
                int i8 = abs2 - i4;
                if (z) {
                    send_linux_mouse_mode_report(i3 * i5, i4 * i6);
                } else {
                    KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
                    JViewerView.this.m_USBMouseRep.set(this.m_btnStatus, i3 * i5, i4 * i6, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
                    kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
                }
                if (i7 > 0 || i8 > 0) {
                    abs2 = i8;
                    abs = i7;
                } else {
                    return;
                }
            }
        }

        private int ABS(int i) {
            return i < 0 ? -i : i;
        }

        private float ABS(float f) {
            return f < 0.0f ? -f : f;
        }

        private void sendto_linux_target(int i, int i2) {
            KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
            JViewerView.this.m_USBMouseRep.set(this.m_btnStatus, i, i2, JViewerView.this.m_USBMouseMode, JViewerView.this.m_width, JViewerView.this.m_height);
            kVMClient.sendKMMessage(JViewerView.this.m_USBMouseRep);
        }

        private int send_linux_mouse_mode_report(int i, int i2) {
            if (ABS(i) + ABS(i2) < JViewerView.ACCEL_THRESHOLD || JViewerView.MOUSE_ACCELERATION <= 1) {
                Debug.out.println("send_linux_mouse_mode_report: " + i + ", " + i2);
                sendto_linux_target(i, i2);
            } else {
                int i3 = i / JViewerView.MOUSE_ACCELERATION;
                int i4 = i2 / JViewerView.MOUSE_ACCELERATION;
                if (!(i3 == 0 && i4 == 0)) {
                    if (ABS(i3) + ABS(i4) >= JViewerView.ACCEL_THRESHOLD) {
                        sendto_linux_target(i3, i4);
                    } else {
                        for (int i5 = 0; i5 < JViewerView.MOUSE_ACCELERATION; i5++) {
                            sendto_linux_target(i3, i4);
                        }
                    }
                }
                int i6 = i % JViewerView.MOUSE_ACCELERATION;
                int i7 = i2 % JViewerView.MOUSE_ACCELERATION;
                if (i6 != 0 || i7 != 0) {
                    if (ABS(i6) + ABS(i7) >= JViewerView.ACCEL_THRESHOLD) {
                        float ABS = ((float) (ABS(i6) + ABS(i7))) / ((float) (JViewerView.ACCEL_THRESHOLD - 1));
                        float f = ((float) i6) / ABS;
                        float f2 = ((float) i7) / ABS;
                        int i8 = 0;
                        int i9 = 0;
                        float f3 = 0.0f;
                        float f4 = 0.0f;
                        float f5 = 0.0f;
                        float f6 = 0.0f;
                        while (true) {
                            if (ABS(f6) >= ((float) ABS(i6)) && ABS(f5) >= ((float) ABS(i7))) {
                                break;
                            }
                            float f7 = f4 + f;
                            float f8 = f3 + f2;
                            sendto_linux_target((int) (f7 - ((float) i9)), (int) (f8 - ((float) i8)));
                            i9 = (int) f7;
                            i8 = (int) f8;
                            f3 = f8;
                            f4 = f7;
                            f5 = f8;
                            f6 = f7;
                        }
                    } else {
                        sendto_linux_target(i6, i7);
                    }
                }
            }
            return 0;
        }
    }

    class RCFocusListener extends FocusAdapter {
        RCFocusListener() {
        }

        public void focusGained(FocusEvent focusEvent) {
        }

        public void focusLost(FocusEvent focusEvent) {
            if (JViewerView.this.m_bLeftCtrlDown) {
                KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
                JViewerView.this.m_USBKeyRep.set(17, 2, false);
                kVMClient.sendKMMessage(JViewerView.this.m_USBKeyRep);
                JViewerView.this.m_bLeftCtrlDown = false;
            }
            if (JViewerView.this.m_bRightCtrlDown) {
                KVMClient kVMClient2 = JViewerApp.getInstance().getKVMClient();
                JViewerView.this.m_USBKeyRep.set(17, 3, false);
                kVMClient2.sendKMMessage(JViewerView.this.m_USBKeyRep);
                JViewerView.this.m_bRightCtrlDown = false;
            }
        }
    }
}
