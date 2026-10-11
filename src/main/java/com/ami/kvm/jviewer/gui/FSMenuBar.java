package com.ami.kvm.jviewer.gui;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FSMenuBar extends JDialog {
    private boolean m_auto;
    private JPanel m_autoPanel;
    private JButton m_autoPin;
    private JButton m_autoUnpin;
    ButtonListener m_btnListener;
    FSFrame m_frame;
    FSMenu m_fsMenu;
    private JLabel m_idLbl;
    private JButton m_menuBtn;
    MouseEnterExit m_msEE;
    JPopupMenu m_popupMenu;
    private boolean m_show;
    private int m_slide = 0;
    private Timer m_timer;

    static /* synthetic */ int access$108(FSMenuBar fSMenuBar) {
        int i = fSMenuBar.m_slide;
        fSMenuBar.m_slide = i + 1;
        return i;
    }

    static /* synthetic */ int access$110(FSMenuBar fSMenuBar) {
        int i = fSMenuBar.m_slide;
        fSMenuBar.m_slide = i - 1;
        return i;
    }

    public FSMenuBar(FSFrame fSFrame) {
        super(fSFrame);
        this.m_frame = fSFrame;
        setBounds((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 200, 0, 400, this.m_slide);
        setUndecorated(true);
        setResizable(false);
        this.m_msEE = new MouseEnterExit();
        addMouseListener(this.m_msEE);
        this.m_btnListener = new ButtonListener();
        JComponent contentPane = getContentPane();
        contentPane.setBackground(new Color(250, 250, 250));
        contentPane.setLayout(new BoxLayout(contentPane, 0));
        contentPane.add(Box.createRigidArea(new Dimension(5, 0)));
        this.m_autoPin = createButton("res/pin.jpg", "Auto Hide", "Pin");
        this.m_autoUnpin = createButton("res/unpin.jpg", "Auto Hide", "Unpin");
        this.m_autoPanel = new JPanel();
        this.m_autoPanel.setLayout(new BoxLayout(this.m_autoPanel, 0));
        this.m_autoPanel.setPreferredSize(new Dimension(15, 15));
        this.m_autoPanel.setMaximumSize(new Dimension(15, 15));
        this.m_autoPanel.add(this.m_autoPin);
        this.m_auto = false;
        contentPane.add(this.m_autoPanel);
        contentPane.add(Box.createRigidArea(new Dimension(50, 0)));
        this.m_menuBtn = createButton("res/menu.jpg", "Menu", "Menu");
        this.m_fsMenu = new FSMenu();
        this.m_popupMenu = this.m_fsMenu.getPopupMenu();
        this.m_popupMenu.addPopupMenuListener(new PopupMenuEventListener());
        this.m_menuBtn.addMouseListener(new PopupListener());
        contentPane.add(this.m_menuBtn);
        contentPane.add(Box.createRigidArea(new Dimension(65, 0)));
        this.m_idLbl = new JLabel("Not connected");
        this.m_idLbl.setForeground(new Color(0, 100, 100));
        this.m_idLbl.setFont(new Font("SansSerif", 0, 12));
        this.m_idLbl.setPreferredSize(new Dimension(150, 15));
        this.m_idLbl.setMaximumSize(new Dimension(150, 15));
        contentPane.add(this.m_idLbl);
        contentPane.add(Box.createRigidArea(new Dimension(48, 0)));
        contentPane.add(createButton("res/min.jpg", "Minimize", "Minimize"));
        contentPane.add(Box.createRigidArea(new Dimension(1, 0)));
        contentPane.add(createButton("res/max.jpg", "Toggle to frame window", "ToFrame"));
        contentPane.add(Box.createRigidArea(new Dimension(1, 0)));
        contentPane.add(createButton("res/close.jpg", "Close", "Close"));
        contentPane.add(Box.createHorizontalGlue());
    }

    public JVMenu getFSMenu() {
        return this.m_fsMenu;
    }

    public void showMenu() {
        this.m_show = true;
        this.m_timer = new Timer();
        this.m_timer.schedule(new SlideTask(), 0, 20);
    }

    public void hideMenu() {
        this.m_show = false;
        this.m_timer = new Timer();
        this.m_timer.schedule(new SlideTask(), 0, 20);
    }

    public void setIDLabel(String str) {
        this.m_idLbl.setText(str);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void slide() {
        setBounds((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 200, 0, 400, this.m_slide);
        setVisible(true);
    }

    private JButton createButton(String str, String str2, String str3) {
        URL resource = JViewer.class.getResource(str);
        JButton jButton = new JButton();
        if (resource != null) {
            jButton.setIcon(new ImageIcon(resource));
        }
        jButton.setToolTipText(str2);
        jButton.setPreferredSize(new Dimension(15, 15));
        jButton.setMaximumSize(new Dimension(15, 15));
        jButton.addMouseListener(this.m_msEE);
        jButton.setActionCommand(str3);
        jButton.addActionListener(this.m_btnListener);
        return jButton;
    }

    /* access modifiers changed from: package-private */
    public class SlideTask extends TimerTask {
        SlideTask() {
        }

        public void run() {
            if (FSMenuBar.this.m_show) {
                if (FSMenuBar.this.m_slide < 20) {
                    FSMenuBar.access$108(FSMenuBar.this);
                    FSMenuBar.this.slide();
                    return;
                }
                FSMenuBar.this.m_timer.cancel();
            } else if (FSMenuBar.this.m_slide > 1) {
                FSMenuBar.access$110(FSMenuBar.this);
                FSMenuBar.this.slide();
            } else {
                FSMenuBar.this.m_timer.cancel();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class MouseEnterExit extends MouseAdapter {
        MouseEnterExit() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            if (FSMenuBar.this.m_auto) {
                FSMenuBar.this.m_timer.cancel();
                FSMenuBar.this.showMenu();
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            if (FSMenuBar.this.m_auto) {
                FSMenuBar.this.m_timer.cancel();
                FSMenuBar.this.hideMenu();
            }
        }
    }

    class PopupListener extends MouseAdapter {
        PopupListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            Debug.out.println("Popup event");
            FSMenuBar.this.m_popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        }
    }

    class PopupMenuEventListener implements PopupMenuListener {
        boolean m_autoCanceled = false;

        PopupMenuEventListener() {
        }

        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            Debug.out.println("Popup canceled");
            if (this.m_autoCanceled) {
                FSMenuBar.this.m_auto = true;
                this.m_autoCanceled = false;
                FSMenuBar.this.hideMenu();
            }
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            Debug.out.println("Popup visible");
            if (FSMenuBar.this.m_auto) {
                FSMenuBar.this.m_auto = false;
                this.m_autoCanceled = true;
            }
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            Debug.out.println("Popup invisible");
            if (this.m_autoCanceled) {
                FSMenuBar.this.m_auto = true;
                this.m_autoCanceled = false;
                FSMenuBar.this.hideMenu();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class ButtonListener implements ActionListener {
        ButtonListener() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JViewerApp instance = JViewerApp.getInstance();
            String actionCommand = actionEvent.getActionCommand();
            if (actionCommand.equals("Unpin")) {
                Debug.out.println("Disable Auto hide");
                FSMenuBar.this.m_autoPanel.removeAll();
                FSMenuBar.this.m_autoPanel.add(FSMenuBar.this.m_autoPin);
                FSMenuBar.this.m_autoPanel.repaint();
                FSMenuBar.this.m_auto = false;
            } else if (actionCommand.equals("Pin")) {
                Debug.out.println("Enable Auto hide");
                FSMenuBar.this.m_autoPanel.removeAll();
                FSMenuBar.this.m_autoPanel.add(FSMenuBar.this.m_autoUnpin);
                FSMenuBar.this.m_auto = true;
            } else if (actionCommand.equals("ToFrame")) {
                Debug.out.println("Toggle to frame window");
                FSMenuBar.this.m_auto = false;
                instance.OnVideoFullScreen(false);
            } else if (actionCommand.equals("Minimize")) {
                Debug.out.println("Minimize");
                FSMenuBar.this.m_frame.setState(1);
            } else if (actionCommand.equals("Close")) {
                Debug.out.println("Close");
                instance.OnVideoExit();
            } else if (actionCommand.equals("Menu")) {
                Debug.out.println("Menu");
            }
        }
    }
}
