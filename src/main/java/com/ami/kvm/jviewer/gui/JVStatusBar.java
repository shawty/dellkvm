package com.ami.kvm.jviewer.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JVStatusBar {
    private JPanel m_statusBar = new JPanel();
    private JLabel m_statusMsg = new JLabel("Keyboard, Video and Mouse redirection");

    public JVStatusBar() {
        this.m_statusBar.setLayout(new BorderLayout());
        this.m_statusBar.add(this.m_statusMsg, "Before");
        this.m_statusBar.setBorder(BorderFactory.createEtchedBorder());
    }

    public JPanel getStatusBar() {
        return this.m_statusBar;
    }

    public void setStatus(String str) {
        this.m_statusMsg.setText(str);
    }

    public String getStatusString() {
        return this.m_statusMsg.getText();
    }

    public void resetStatus() {
        this.m_statusMsg.setText("Keyboard, Video and Mouse redirection");
    }
}
