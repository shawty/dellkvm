package com.ami.kvm.jviewer.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AutoBWDlg extends JDialog {
    JLabel m_msg = new JLabel("Detecting bandwidth, please wait...");

    public AutoBWDlg(JFrame jFrame) {
        super(jFrame, "Detecting bandwidth...", false);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 0));
        jPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        jPanel.add(this.m_msg);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 1));
        contentPane.add(jPanel);
        setSize(300, 100);
        setLocationRelativeTo(null);
    }

    public void setMessage(String str) {
        this.m_msg.setText(str);
    }

    public void done() {
        try {
            Thread.sleep(1000);
            this.m_msg.setText("Changing the bandwidth settings..");
            Thread.sleep(1000);
            this.m_msg.setText("Done!");
            Thread.sleep(200);
        } catch (Exception e) {
        }
        dispose();
    }
}
