package com.ami.kvm.jviewer.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public abstract class JVFrame extends JFrame {
    protected static String m_serverIP;
    protected JScrollPane m_viewSP;

    public abstract void exitApp();

    public abstract JVMenu getMenu();

    public abstract void refreshTitle();

    public abstract void resetStatus();

    public abstract void setStatus(String str);

    public abstract void setWndLabel(String str);

    public static void setServerIP(byte[] bArr) {
        try {
            m_serverIP = InetAddress.getByAddress(bArr).getHostAddress();
        } catch (UnknownHostException e) {
            m_serverIP = "Not connected";
        }
    }

    public static String getServerIP() {
        if (m_serverIP == null || m_serverIP.equalsIgnoreCase(new String("Not connected"))) {
            return null;
        }
        return m_serverIP;
    }

    public void attachView() {
        this.m_viewSP = new JScrollPane(JViewerApp.getInstance().getRCView());
        this.m_viewSP.getHorizontalScrollBar().setUnitIncrement(10);
        this.m_viewSP.getVerticalScrollBar().setUnitIncrement(10);
        getContentPane().add(this.m_viewSP);
        setVisible(true);
    }

    public void detachView() {
        getContentPane().remove(this.m_viewSP);
        setVisible(false);
    }

    /* access modifiers changed from: protected */
    public void windowClosed() {
        System.exit(0);
    }

    public void generalErrorMessage(String str, String str2) {
        JOptionPane.showMessageDialog(this, str2, str, 0);
    }
}
