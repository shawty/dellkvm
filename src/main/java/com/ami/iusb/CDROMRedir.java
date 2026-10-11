package com.ami.iusb;

import com.ami.iusb.protocol.CDROMProtocol;
import com.ami.iusb.protocol.IUSBSCSI;
import com.ami.iusb.protocol.PacketMaster;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.JViewerApp;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CDROMRedir extends Thread {
    private static final int CONNECTION_ACCEPTED = 1;
    private static final int CONNECTION_DENIED = 2;
    private static final int DEVICE_REDIRECTION_ACK = 241;
    private static final int MAX_READ_SECTORS = 64;
    private static final int MAX_READ_SIZE = 131072;
    private static final int PORT = 5120;
    private static final int START_LOCAL_IMAGE_REDIRECTION = 240;
    private long nativeReaderPointer = -1;
    private PacketMaster packetMaster;
    private ByteBuffer packetReadBuffer;
    private ByteBuffer packetWriteBuffer;
    private boolean physicalDrive;
    private CDROMProtocol protocol;
    private Thread redirThread;
    private boolean running = false;
    private boolean stopRunning = false;

    private native void closeCDROM();

    private native void deleteCDROMReader();

    private native int executeCDROMSCSICmd(ByteBuffer byteBuffer, ByteBuffer byteBuffer2);

    private native String getVersion();

    private native String[] listCDROMDrives();

    private native void newCDROMReader(boolean z);

    private native boolean openCDROM(String str);

    static {
        try {
            System.loadLibrary("javacdromwrapper");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot find cdrom wrapper library");
        }
    }

    public CDROMRedir(boolean z) {
        this.physicalDrive = z;
        this.protocol = new CDROMProtocol();
        this.packetReadBuffer = ByteBuffer.allocateDirect(1024);
        this.packetWriteBuffer = ByteBuffer.allocateDirect(131133);
    }

    private void cdromConnect(String str) throws IOException {
        this.packetMaster = new PacketMaster(str, PORT, false, this.protocol, false);
        this.packetMaster.setupBuffers(this.packetReadBuffer, this.packetWriteBuffer);
        this.packetMaster.setBufferEndianness(ByteOrder.LITTLE_ENDIAN, ByteOrder.LITTLE_ENDIAN);
        this.packetMaster.connect();
    }

    private void cdromDisconnect() {
        try {
            this.packetMaster.close();
        } catch (IOException e) {
            System.err.println("Error closing CDROM connection: " + e.getMessage());
        }
    }

    public boolean startRedirection(String str, String str2) throws RedirectionException {
        if (this.running) {
            return true;
        }
        try {
            cdromConnect(str);
            IUSBSCSI recvRequest = recvRequest();
            if (recvRequest.opcode != DEVICE_REDIRECTION_ACK) {
                cdromDisconnect();
                throw new RedirProtocolException("Got unexpected opcode: " + recvRequest.opcode);
            } else if (recvRequest.connectionStatus != 1) {
                cdromDisconnect();
                if (recvRequest.m_otherIP != null) {
                    JViewerApp.getInstance().getMainWindow().generalErrorMessage("CDROM redirection", "CDROM Redirection is already in use.\nIf the client that is doing CDROM redirection has\nlost connectivity to the card, it may take upto 3 minutes\nfor the card to discard the old connection and\naccept a new connection.\nCDROM redirection is on from client with IP : " + recvRequest.m_otherIP);
                }
                return false;
            } else {
                if (this.nativeReaderPointer == -1) {
                    newCDROMReader(this.physicalDrive);
                }
                if (!openCDROM(str2)) {
                    System.err.println("Cannot open CDROM!");
                    deleteCDROMReader();
                    cdromDisconnect();
                    return false;
                }
                this.redirThread = new Thread(this);
                this.redirThread.start();
                this.running = true;
                return true;
            }
        } catch (IOException e) {
            throw new RedirectionException(e.getMessage());
        }
    }

    public boolean stopRedirection() {
        if (this.running) {
            this.stopRunning = true;
            try {
                this.packetMaster.wakeup();
                this.redirThread.join();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while joining cdrom thread");
            }
            cdromDisconnect();
            this.running = false;
            this.stopRunning = false;
            closeCDROM();
            deleteCDROMReader();
        }
        return true;
    }

    private IUSBSCSI recvRequest() throws IOException, RedirectionException {
        return (IUSBSCSI) this.packetMaster.receivePacket();
    }

    public boolean isRedirActive() {
        return this.running;
    }

    public void stopRedirectionAbnormal() {
        if (this.running) {
            this.stopRunning = true;
            cdromDisconnect();
            this.running = false;
            this.stopRunning = false;
            closeCDROM();
            deleteCDROMReader();
            if (this.physicalDrive) {
                JViewerApp.getInstance().getJVMenu().notifyCDROMRedir(false);
            } else {
                JViewerApp.getInstance().getJVMenu().notifyISORedir(false);
            }
        }
    }

    public void run() {
        while (!this.stopRunning) {
            try {
                this.packetWriteBuffer.rewind();
                if (recvRequest() != null) {
                    this.packetWriteBuffer.limit(executeCDROMSCSICmd(this.packetReadBuffer, this.packetWriteBuffer));
                    this.packetMaster.sendPacket(new IUSBSCSI(this.packetWriteBuffer, true));
                }
            } catch (IOException e) {
                if (!this.stopRunning) {
                    stopRedirectionAbnormal();
                    handleError(e.getMessage());
                    return;
                }
            } catch (RedirectionException e2) {
                if (!this.stopRunning) {
                    stopRedirectionAbnormal();
                    handleError(e2.getMessage());
                    return;
                }
            }
        }
        Debug.out.println("Exiting the CDROM/ISO Redirection thread");
    }

    public String[] getCDROMList() throws RedirectionException {
        if (!this.physicalDrive) {
            DisplayErrorMsg("Cannot get CDROM list from ISO redirection session");
            return null;
        }
        if (this.nativeReaderPointer == -1) {
            newCDROMReader(true);
        }
        String[] listCDROMDrives = listCDROMDrives();
        if (this.nativeReaderPointer != -1) {
            return listCDROMDrives;
        }
        deleteCDROMReader();
        return listCDROMDrives;
    }

    public String getLIBCDROMVersion() {
        if (this.nativeReaderPointer != -1) {
            return getVersion();
        }
        newCDROMReader(false);
        String version = getVersion();
        deleteCDROMReader();
        return version;
    }

    public void DisplayErrorMsg(String str) {
        JViewerApp.getInstance().getMainWindow().generalErrorMessage("Device Redirection", str);
    }

    public void handleError(String str) {
        DisplayErrorMsg(str);
    }

    public boolean isPhysicalDevice() {
        return this.physicalDrive;
    }
}
