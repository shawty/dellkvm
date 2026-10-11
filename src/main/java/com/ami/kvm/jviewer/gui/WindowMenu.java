package com.ami.kvm.jviewer.gui;

import javax.swing.JMenuBar;

public class WindowMenu extends JVMenu {
    private JMenuBar m_menuBar;

    public WindowMenu() {
        constructUserIf();
    }

    public JMenuBar getMenuBar() {
        return this.m_menuBar;
    }

    private void constructUserIf() {
        this.m_menuBar = new JMenuBar();
        this.m_menuBar.add(constructVideoMenu());
        this.m_menuBar.add(constructKeyboardMenu());
        this.m_menuBar.add(constructMouseMenu());
        this.m_menuBar.add(constructOptionsMenu());
        this.m_menuBar.add(constructDeviceRedirMenu());
        this.m_menuBar.add(constructHelpMenu());
    }
}
