package com.ami.kvm.jviewer.gui;

import java.awt.Toolkit;

public class FSFrame extends JVFrame {
    private String m_label;
    private FSMenuBar m_menuBar = new FSMenuBar(this);

    public FSFrame() {
        setUndecorated(true);
        setResizable(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public JVMenu getMenu() {
        return this.m_menuBar.getFSMenu();
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void setStatus(String str) {
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void resetStatus() {
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void exitApp() {
        windowClosed();
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void setWndLabel(String str) {
        this.m_label = str;
        this.m_menuBar.setIDLabel(m_serverIP + " - " + str);
    }

    public void show() {
        super.show();
        this.m_menuBar = new FSMenuBar(this);
        this.m_menuBar.showMenu();
    }

    public void hide() {
        this.m_menuBar.dispose();
        super.hide();
    }

    @Override // com.ami.kvm.jviewer.gui.JVFrame
    public void refreshTitle() {
        this.m_menuBar.setIDLabel(m_serverIP + " - " + this.m_label);
    }
}
