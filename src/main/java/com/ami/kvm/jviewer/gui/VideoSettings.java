package com.ami.kvm.jviewer.gui;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.hid.KVMClient;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.video.ColorGains;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VideoSettings extends JDialog implements ChangeListener, ActionListener, WindowListener {
    private static final String CMD_AUTOCALIBER = "autocaliber";
    public static final String CMD_COLOR_GAINS = "colorgains";
    private static final String CMD_DEFAULT = "default";
    private static final String CMD_DOWN = "down";
    private static final String CMD_LEFT = "left";
    private static final String CMD_RIGHT = "right";
    private static final String CMD_UP = "up";
    public static final int EVENT_COLOR_GAINS = 2000;
    public static final int GAIN_MAX = 255;
    public static final int GAIN_MIN = 0;
    private static VideoSettings _instance = null;
    private JButton m_autoCaliber;
    private int m_blueDef;
    private JSlider m_blueGain;
    private JButton m_defGains;
    private JButton m_down;
    private int m_greenDef;
    private JSlider m_greenGain;
    private JButton m_left;
    private int m_redDef;
    private JSlider m_redGain;
    private JButton m_right;
    private boolean m_settingGUI = false;
    private JButton m_up;

    public static synchronized VideoSettings getInstance(JFrame jFrame, int i, int i2, int i3) {
        VideoSettings videoSettings;
        synchronized (VideoSettings.class) {
            if (_instance == null) {
                _instance = new VideoSettings(jFrame, i, i2, i3);
                videoSettings = _instance;
            } else {
                videoSettings = _instance;
            }
        }
        return videoSettings;
    }

    private VideoSettings(JFrame jFrame, int i, int i2, int i3) {
        super(jFrame, "Video settings", false);
        this.m_redDef = i;
        this.m_greenDef = i2;
        this.m_blueDef = i3;
        this.m_redGain = new JSlider(0, 0, (int) GAIN_MAX, i);
        this.m_greenGain = new JSlider(0, 0, (int) GAIN_MAX, i2);
        this.m_blueGain = new JSlider(0, 0, (int) GAIN_MAX, i3);
        this.m_redGain.setEnabled(false);
        this.m_greenGain.setEnabled(false);
        this.m_blueGain.setEnabled(false);
        this.m_redGain.addChangeListener(this);
        this.m_greenGain.addChangeListener(this);
        this.m_blueGain.addChangeListener(this);
        this.m_left = new JButton();
        setBtnImage(this.m_left, "res/left.jpg");
        this.m_right = new JButton();
        setBtnImage(this.m_right, "res/right.jpg");
        this.m_up = new JButton();
        setBtnImage(this.m_up, "res/up.jpg");
        this.m_down = new JButton();
        setBtnImage(this.m_down, "res/down.jpg");
        this.m_left.setActionCommand(CMD_LEFT);
        this.m_right.setActionCommand(CMD_RIGHT);
        this.m_up.setActionCommand(CMD_UP);
        this.m_down.setActionCommand(CMD_DOWN);
        this.m_left.addActionListener(this);
        this.m_right.addActionListener(this);
        this.m_up.addActionListener(this);
        this.m_down.addActionListener(this);
        this.m_defGains = new JButton("Default gains");
        this.m_autoCaliber = new JButton("Autocalibrate");
        this.m_defGains.setActionCommand(CMD_DEFAULT);
        this.m_autoCaliber.setActionCommand(CMD_AUTOCALIBER);
        this.m_defGains.addActionListener(this);
        this.m_autoCaliber.addActionListener(this);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, 1));
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(constructUserIf());
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        setSize(350, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setGains(ColorGains colorGains) {
        this.m_redDef = colorGains.redDefault;
        this.m_greenDef = colorGains.greenDefault;
        this.m_blueDef = colorGains.blueDefault;
        this.m_redGain.setEnabled(true);
        this.m_greenGain.setEnabled(true);
        this.m_blueGain.setEnabled(true);
        this.m_settingGUI = true;
        this.m_redGain.setValue(colorGains.redCurrent);
        this.m_greenGain.setValue(colorGains.greenCurrent);
        this.m_blueGain.setValue(colorGains.blueCurrent);
        this.m_settingGUI = false;
    }

    private JPanel constructUserIf() {
        this.m_defGains.setSize(120, 30);
        this.m_defGains.setPreferredSize(new Dimension(120, 30));
        this.m_defGains.setMaximumSize(new Dimension(120, 30));
        this.m_autoCaliber.setSize(120, 30);
        this.m_autoCaliber.setPreferredSize(new Dimension(120, 30));
        this.m_autoCaliber.setMaximumSize(new Dimension(120, 30));
        this.m_redGain.setSize(200, 30);
        this.m_redGain.setPreferredSize(new Dimension(200, 30));
        this.m_redGain.setMaximumSize(new Dimension(200, 30));
        this.m_greenGain.setSize(200, 30);
        this.m_greenGain.setPreferredSize(new Dimension(200, 30));
        this.m_greenGain.setMaximumSize(new Dimension(200, 30));
        this.m_blueGain.setSize(200, 30);
        this.m_blueGain.setPreferredSize(new Dimension(200, 30));
        this.m_blueGain.setMaximumSize(new Dimension(200, 30));
        this.m_left.setSize(20, 20);
        this.m_left.setPreferredSize(new Dimension(20, 20));
        this.m_left.setMaximumSize(new Dimension(20, 20));
        this.m_right.setSize(20, 20);
        this.m_right.setPreferredSize(new Dimension(20, 20));
        this.m_right.setMaximumSize(new Dimension(20, 20));
        this.m_up.setSize(20, 20);
        this.m_up.setPreferredSize(new Dimension(20, 20));
        this.m_up.setMaximumSize(new Dimension(20, 20));
        this.m_down.setSize(20, 20);
        this.m_down.setPreferredSize(new Dimension(20, 20));
        this.m_down.setMaximumSize(new Dimension(20, 20));
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel.add(jPanel2);
        jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel.add(jPanel3);
        jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel.add(jPanel4);
        jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Color gains"));
        jPanel2.setLayout(new BoxLayout(jPanel2, 1));
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        jPanel5.add(Box.createRigidArea(new Dimension(105, 0)));
        jPanel5.add(new JLabel("Low"));
        jPanel5.add(Box.createHorizontalGlue());
        jPanel5.add(new JLabel("High"));
        jPanel5.add(Box.createRigidArea(new Dimension(15, 0)));
        jPanel2.add(Box.createRigidArea(new Dimension(0, 5)));
        jPanel2.add(jPanel5);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BoxLayout(jPanel6, 0));
        jPanel6.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel6.add(new JLabel("Red gain"));
        jPanel6.add(Box.createHorizontalGlue());
        jPanel6.add(this.m_redGain);
        jPanel6.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel2.add(jPanel6);
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new BoxLayout(jPanel7, 0));
        jPanel7.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel7.add(new JLabel("Green gain"));
        jPanel7.add(Box.createHorizontalGlue());
        jPanel7.add(this.m_greenGain);
        jPanel7.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel2.add(jPanel7);
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new BoxLayout(jPanel8, 0));
        jPanel8.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel8.add(new JLabel("Blue gain"));
        jPanel8.add(Box.createHorizontalGlue());
        jPanel8.add(this.m_blueGain);
        jPanel8.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel2.add(jPanel8);
        jPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel2.setPreferredSize(new Dimension(320, 160));
        jPanel2.setMaximumSize(new Dimension(320, 160));
        jPanel3.setBorder(BorderFactory.createTitledBorder("Screen position"));
        jPanel3.setLayout(new BoxLayout(jPanel3, 1));
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new BoxLayout(jPanel9, 0));
        jPanel9.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel9.add(new JLabel("Horizontal position"));
        jPanel9.add(Box.createHorizontalGlue());
        jPanel9.add(this.m_left);
        jPanel9.add(this.m_right);
        jPanel9.add(Box.createRigidArea(new Dimension(30, 0)));
        jPanel3.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel3.add(jPanel9);
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new BoxLayout(jPanel10, 0));
        jPanel10.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel10.add(new JLabel("Vertical position"));
        jPanel10.add(Box.createHorizontalGlue());
        jPanel10.add(this.m_down);
        jPanel10.add(this.m_up);
        jPanel10.add(Box.createRigidArea(new Dimension(30, 0)));
        jPanel3.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel3.add(jPanel10);
        jPanel3.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel3.setPreferredSize(new Dimension(320, 100));
        jPanel3.setMaximumSize(new Dimension(320, 100));
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        jPanel4.add(Box.createRigidArea(new Dimension(30, 0)));
        jPanel4.add(this.m_defGains);
        jPanel4.add(Box.createHorizontalGlue());
        jPanel4.add(this.m_autoCaliber);
        jPanel4.add(Box.createRigidArea(new Dimension(30, 0)));
        jPanel4.setPreferredSize(new Dimension(320, 40));
        jPanel4.setMaximumSize(new Dimension(320, 40));
        return jPanel;
    }

    public void stateChanged(ChangeEvent changeEvent) {
        if (!this.m_settingGUI && !((JSlider) changeEvent.getSource()).getValueIsAdjusting()) {
            JViewerApp.getInstance().getKVMClient().setColorGains((byte) this.m_redGain.getValue(), (byte) this.m_greenGain.getValue(), (byte) this.m_blueGain.getValue());
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        Debug.out.println(actionCommand);
        KVMClient kVMClient = JViewerApp.getInstance().getKVMClient();
        if (actionCommand.equals(CMD_LEFT)) {
            kVMClient.moveScreen(IVTPPktHdr.ADVISER_SHIFT_IMAGE_LEFT);
        } else if (actionCommand.equals(CMD_RIGHT)) {
            kVMClient.moveScreen(IVTPPktHdr.ADVISER_SHIFT_IMAGE_RIGHT);
        } else if (actionCommand.equals(CMD_UP)) {
            kVMClient.moveScreen((byte) 20);
        } else if (actionCommand.equals(CMD_DOWN)) {
            kVMClient.moveScreen(IVTPPktHdr.ADVISER_SHIFT_IMAGE_DOWN);
        } else if (actionCommand.equals(CMD_DEFAULT)) {
            onSetDefault();
        } else if (actionCommand.equals(CMD_AUTOCALIBER)) {
            onAutocalibrate();
        }
    }

    public void onSetDefault() {
        Debug.out.println("Set default");
        this.m_settingGUI = true;
        this.m_redGain.setValue(this.m_redDef);
        this.m_greenGain.setValue(this.m_greenDef);
        this.m_blueGain.setValue(this.m_blueDef);
        JViewerApp.getInstance().getKVMClient().setDefaultColorGains();
        this.m_settingGUI = false;
    }

    public void onAutocalibrate() {
        Debug.out.println("Autocalibrate");
        JViewerApp.getInstance().getKVMClient().autocalibrate();
    }

    private void setBtnImage(JButton jButton, String str) {
        URL resource = JViewer.class.getResource(str);
        if (resource != null) {
            jButton.setIcon(new ImageIcon(resource));
        }
    }

    public void windowOpened(WindowEvent windowEvent) {
        _instance = this;
    }

    public void windowClosed(WindowEvent windowEvent) {
        _instance = null;
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }
}
