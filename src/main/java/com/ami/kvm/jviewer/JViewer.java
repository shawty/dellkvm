package com.ami.kvm.jviewer;

import com.ami.kvm.jviewer.gui.JViewerApp;
import java.awt.Component;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.JOptionPane;

public class JViewer {
    public static void main(String[] strArr) throws IOException {
        if (strArr.length < 3) {
            printUsage();
            return;
        }
        byte[] serverIP = getServerIP(InetAddress.getByName(strArr[0]).getHostAddress());
        if (serverIP == null) {
            printUsage();
            return;
        }
        try {
            JViewerApp.getInstance().OnConnectToServer(serverIP, Integer.parseInt(strArr[1]), strArr[2]);
        } catch (NumberFormatException e) {
            printUsage();
        }
    }

    public static void printUsage() {
        JOptionPane.showMessageDialog((Component) null, "Usage:\n\t java RConsoleApp <Aster Card IP><Port>");
        System.exit(0);
    }

    private static byte[] getServerIP(String str) {
        if (str.trim().length() <= 0) {
            return null;
        }
        String[] split = str.split("\\.");
        if (split.length != 4) {
            return null;
        }
        byte[] bArr = new byte[4];
        for (int i = 0; i < 4; i++) {
            try {
                bArr[i] = (byte) Short.parseShort(split[i]);
                if (bArr[i] > 255) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return bArr;
    }
}
