package com.ami.iusb;

import com.ami.kvm.jviewer.gui.JVFrame;
import com.ami.kvm.jviewer.gui.JViewerApp;
import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class IUSBRedirSession {
    public static final int CMD_STATUS_BUSY = 1;
    public static final int CMD_STATUS_FREE = 2;
    public CDROMRedir cdromSession = null;
    public FloppyRedir floppySession = null;
    private String host = null;
    private int m_CDROMRedirCmdStatus = 2;
    private int m_FloppyImageRedirCmdStatus = 2;
    private int m_FloppyRedirCmdStatus = 2;
    private int m_ISORedirCmdStatus = 2;

    public String getHost() {
        return this.host;
    }

    public boolean StartCDROMRedir() {
        JViewerApp.getInstance().getMainWindow();
        this.host = JVFrame.getServerIP();
        if (this.host == null) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", "Invalid IP Address");
            return false;
        }
        try {
            if (this.cdromSession != null) {
                JViewerApp.getInstance().getMainWindow().generalErrorMessage("CDROM Redirection Error", "Previous CDROM/ISO redirection session is not closed properly");
                return false;
            }
            this.cdromSession = new CDROMRedir(true);
            String[] cDROMList = this.cdromSession.getCDROMList();
            if (cDROMList != null) {
                String promptCDROM = promptCDROM(cDROMList);
                if (promptCDROM == null) {
                    StopCDROMRedir();
                    return false;
                } else if (this.cdromSession.startRedirection(this.host, promptCDROM)) {
                    return true;
                } else {
                    JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", "CD-ROM redirection failed to start");
                    StopCDROMRedir();
                    return false;
                }
            } else {
                JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", "No CD-ROMs found.  Please insert a CD-ROM and try again");
                StopCDROMRedir();
                return false;
            }
        } catch (UnsatisfiedLinkError e) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", "CD-ROM redirection is not supported on this platform");
            StopCDROMRedir();
            return false;
        } catch (RedirectionException e2) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", e2.getMessage());
            StopCDROMRedir();
            return false;
        }
    }

    public boolean StopCDROMRedir() {
        if (this.cdromSession != null) {
            if (!this.cdromSession.stopRedirection()) {
                JViewerApp.getInstance().getMainWindow().generalErrorMessage("Close CDROM/ISO redirection", "Unable to stop CDROM/ISO Redirection. Please try again.");
                return false;
            }
            this.cdromSession = null;
            System.gc();
        }
        return true;
    }

    public void SetCDROMRedirStopped() {
        JViewerApp.getInstance().getJVMenu().notifyCDROMRedir(false);
        SetCDROMRedirCmdStatus(2);
    }

    public void SetCDROMRedirCmdStatus(int i) {
        this.m_CDROMRedirCmdStatus = i;
    }

    public boolean Is_CDROMRedirCmdStatus_FREE() {
        if (this.m_CDROMRedirCmdStatus == 2) {
            return true;
        }
        return false;
    }

    public boolean StartISORedir() {
        JViewerApp.getInstance().getMainWindow();
        this.host = JVFrame.getServerIP();
        if (this.host == null) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM", "Invalid IP Address");
            return false;
        }
        try {
            if (this.cdromSession != null) {
                JViewerApp.getInstance().getMainWindow().generalErrorMessage("CDROM Redirection Error", "Previous CDROM/ISO redirection session is not closed properly");
                return false;
            }
            String isoImageSelector = isoImageSelector();
            if (isoImageSelector == null) {
                return false;
            }
            this.cdromSession = new CDROMRedir(false);
            if (this.cdromSession.startRedirection(this.host, isoImageSelector)) {
                return true;
            }
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM image", "CD-ROM image redirection failed to start");
            StopISORedir();
            return false;
        } catch (UnsatisfiedLinkError e) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM image", "CD-ROM image redirection is not supported on this platform");
            StopISORedir();
            return false;
        } catch (RedirectionException e2) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect CD-ROM image", e2.getMessage());
            StopISORedir();
            return false;
        }
    }

    public boolean StopISORedir() {
        return StopCDROMRedir();
    }

    public void SetISORedirStopped() {
        JViewerApp.getInstance().getJVMenu().notifyISORedir(false);
        SetISORedirCmdStatus(2);
    }

    public void SetISORedirCmdStatus(int i) {
        this.m_ISORedirCmdStatus = i;
    }

    public boolean Is_ISORedirCmdStatus_FREE() {
        if (this.m_ISORedirCmdStatus == 2) {
            return true;
        }
        return false;
    }

    public boolean StartFloppyRedir() {
        JViewerApp.getInstance().getMainWindow();
        this.host = JVFrame.getServerIP();
        if (this.host == null) {
            JOptionPane.showMessageDialog((Component) null, "Invalid IP Address");
            return false;
        }
        try {
            if (this.floppySession != null) {
                if (this.floppySession.isRedirActive()) {
                    JOptionPane.showMessageDialog((Component) null, "Previous Floppy/Floppy Image redirection session is not closed properly");
                    return false;
                }
                StopFloppyRedir();
            }
            this.floppySession = new FloppyRedir(true);
            String[] floppyList = this.floppySession.getFloppyList();
            if (floppyList != null) {
                String promptFloppy = promptFloppy(floppyList);
                if (promptFloppy == null) {
                    StopFloppyRedir();
                    return false;
                } else if (this.floppySession.startRedirection(this.host, promptFloppy)) {
                    return true;
                } else {
                    JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect Floppy", "Floppy redirection failed to start");
                    StopFloppyRedir();
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog((Component) null, "No Floppys found.  Please insert a Floppy and try again");
                StopFloppyRedir();
                return false;
            }
        } catch (UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog((Component) null, "Floppy redirection is not supported on this platform");
            StopFloppyRedir();
            return false;
        } catch (RedirectionException e2) {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect Floppy", e2.getMessage());
            StopFloppyRedir();
            return false;
        }
    }

    public boolean StopFloppyRedir() {
        if (this.floppySession != null) {
            if (!this.floppySession.stopRedirection()) {
                JOptionPane.showMessageDialog((Component) null, "Unable to stop Floppy/Floppy Image Redirection. Please try again.");
                return false;
            }
            this.floppySession = null;
            System.gc();
        }
        return true;
    }

    public void SetFloppyRedirStopped() {
        JViewerApp.getInstance().getJVMenu().notifyFloppyRedir(false);
        SetFloppyRedirCmdStatus(2);
    }

    public void SetFloppyRedirCmdStatus(int i) {
        this.m_FloppyRedirCmdStatus = i;
    }

    public boolean Is_FloppyRedirCmdStatus_FREE() {
        if (this.m_FloppyRedirCmdStatus == 2) {
            return true;
        }
        return false;
    }

    public boolean StartFloppyImageRedir() {
        JViewerApp.getInstance().getMainWindow();
        this.host = JVFrame.getServerIP();
        if (this.host == null) {
            JOptionPane.showMessageDialog((Component) null, "Invalid IP Address");
            return false;
        }
        try {
            if (this.floppySession != null) {
                StopFloppyImageRedir();
                this.floppySession = null;
            }
            String floppyImageSelector = floppyImageSelector();
            if (floppyImageSelector == null) {
                return false;
            }
            this.floppySession = new FloppyRedir(false);
            if (this.floppySession.startRedirection(this.host, floppyImageSelector)) {
                return true;
            }
            JViewerApp.getInstance().getMainWindow().generalErrorMessage("Cannot redirect Floppy image", "Floppy image redirection failed to start");
            StopFloppyImageRedir();
            return false;
        } catch (UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog((Component) null, "Floppy image redirection is not supported on this platform");
            StopFloppyImageRedir();
            return false;
        } catch (RedirectionException e2) {
            JOptionPane.showMessageDialog((Component) null, e2.getMessage());
            StopFloppyImageRedir();
            return false;
        }
    }

    public boolean StopFloppyImageRedir() {
        return StopFloppyRedir();
    }

    public void SetFloppyImageRedirStopped() {
        JViewerApp.getInstance().getJVMenu().notifyFloppyImageRedir(false);
        SetFloppyImageRedirCmdStatus(2);
    }

    public void SetFloppyImageRedirCmdStatus(int i) {
        this.m_FloppyImageRedirCmdStatus = i;
    }

    public boolean Is_FloppyImageRedirCmdStatus_FREE() {
        if (this.m_FloppyImageRedirCmdStatus == 2) {
            return true;
        }
        return false;
    }

    public String getLIBCDROMVersion() {
        try {
            if (this.cdromSession != null) {
                return this.cdromSession.getLIBCDROMVersion();
            }
            this.cdromSession = new CDROMRedir(false);
            String lIBCDROMVersion = this.cdromSession.getLIBCDROMVersion();
            this.cdromSession = null;
            System.gc();
            return lIBCDROMVersion;
        } catch (UnsatisfiedLinkError e) {
            return "Not present";
        }
    }

    public String promptCDROM(String[] strArr) {
        JVFrame mainWindow = JViewerApp.getInstance().getMainWindow();
        return (String) JListDialog.showDialog(mainWindow, mainWindow, "Choose a CD-ROM drive to redirect", "CD-ROM Selection", strArr, null);
    }

    /* access modifiers changed from: package-private */
    public class IsoImageFilter extends FileFilter {
        IsoImageFilter() {
        }

        public boolean accept(File file) {
            return file.getName().endsWith(".iso") || file.isDirectory();
        }

        public String getDescription() {
            return "*.iso";
        }
    }

    public String isoImageSelector() {
        JVFrame mainWindow = JViewerApp.getInstance().getMainWindow();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new IsoImageFilter());
        if (jFileChooser.showOpenDialog(mainWindow) == 0) {
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public String getLIBFLOPPYVersion() {
        try {
            if (this.floppySession != null) {
                return this.floppySession.getLIBFLOPPYVersion();
            }
            this.floppySession = new FloppyRedir(false);
            String lIBFLOPPYVersion = this.floppySession.getLIBFLOPPYVersion();
            this.floppySession = null;
            System.gc();
            return lIBFLOPPYVersion;
        } catch (UnsatisfiedLinkError e) {
            return "Not present";
        }
    }

    public String promptFloppy(String[] strArr) {
        JVFrame mainWindow = JViewerApp.getInstance().getMainWindow();
        return (String) JListDialog.showDialog(mainWindow, mainWindow, "Choose a floppy drive to redirect", "Floppy Selection", "Or enter a floppy drive manually", strArr, null);
    }

    /* access modifiers changed from: package-private */
    public class FloppyImageFilter extends FileFilter {
        FloppyImageFilter() {
        }

        public boolean accept(File file) {
            return file.getName().endsWith(".img") || file.isDirectory();
        }

        public String getDescription() {
            return "*.img";
        }
    }

    public String floppyImageSelector() {
        JVFrame mainWindow = JViewerApp.getInstance().getMainWindow();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FloppyImageFilter());
        if (jFileChooser.showOpenDialog(mainWindow) == 0) {
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
