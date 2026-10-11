package com.ami.kvm.jviewer.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowFrame extends JVFrame {
    private String m_label;
    private WindowMenu m_menu;
    private JVStatusBar m_status;

    public WindowFrame() {
        setTitle("KVM Remote Console");
        setSize(new Dimension(1024, 768));
        constructUserIf();
        addWindowListener(new WindowAdapter() {
            /* class com.ami.kvm.jviewer.gui.WindowFrame.AnonymousClass1 */

            public void windowClosing(WindowEvent windowEvent) {
                WindowFrame.this.windowClosed();
            }
        });
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public JVMenu getMenu() {
        return this.m_menu;
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void setStatus(String str) {
        this.m_status.setStatus(str);
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void resetStatus() {
        this.m_status.resetStatus();
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void exitApp() {
        windowClosed();
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void refreshTitle() {
        setTitle("JViewer[" + m_serverIP + "] - " + this.m_label);
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void setWndLabel(String str) {
        this.m_label = str;
        setTitle("JViewer[" + m_serverIP + "] - " + str);
    }

    private void constructUserIf() {
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        this.m_menu = new WindowMenu();
        setJMenuBar(this.m_menu.getMenuBar());
        this.m_status = new JVStatusBar();
        contentPane.add(this.m_status.getStatusBar(), "Last");
        this.m_status.setStatus("KVM Remote Console started... ");
    }
}
