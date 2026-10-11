package com.ami.kvm.jviewer.gui;

import javax.swing.JPopupMenu;

public class FSMenu extends JVMenu {
    private JPopupMenu m_popupMenu;

    public FSMenu() {
        constructUserIf();
    }

    public JPopupMenu getPopupMenu() {
        return this.m_popupMenu;
    }

    private void constructUserIf() {
        this.m_popupMenu = new JPopupMenu();
        this.m_popupMenu.add(constructVideoMenu());
        this.m_popupMenu.add(constructKeyboardMenu());
        this.m_popupMenu.add(constructMouseMenu());
        this.m_popupMenu.add(constructOptionsMenu());
        this.m_popupMenu.add(constructDeviceRedirMenu());
        this.m_popupMenu.add(constructHelpMenu());
    }
}
