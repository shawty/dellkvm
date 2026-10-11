package com.ami.iusb;

import com.ami.iusb.protocol.FloppyProtocol;
import com.ami.iusb.protocol.IUSBSCSI;
import com.ami.iusb.protocol.PacketMaster;
import com.ami.kvm.jviewer.gui.JViewerApp;
import java.awt.Component;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.swing.JOptionPane;

public class FloppyRedir extends Thread {
    private static final int CONNECTION_ACCEPTED = 1;
    private static final int CONNECTION_DENIED = 2;
    private static final int DEVICE_REDIRECTION_ACK = 241;
    private static final int MAX_READ_SECTORS = 256;
    private static final int MAX_READ_SIZE = 131072;
    private static final int PORT = 5123;
    private static final int START_LOCAL_IMAGE_REDIRECTION = 240;
    private long nativeReaderPointer = -1;
    private PacketMaster packetMaster;
    private ByteBuffer packetReadBuffer;
    private ByteBuffer packetWriteBuffer;
    private boolean physicalDevice;
    private FloppyProtocol protocol;
    private Thread redirThread;
    private boolean running = false;
    private boolean stopRunning = false;

    private native void closeFloppy();

    private native void deleteFloppyReader();

    private native int executeFloppySCSICmd(ByteBuffer byteBuffer, ByteBuffer byteBuffer2);

    private native String getVersion();

    private native String[] listFloppyDrives();

    private native void newFloppyReader(boolean z);

    private native boolean openFloppy(String str);

    static {
        try {
            System.loadLibrary("javafloppywrapper");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot find floppy wrapper library dfdsfdsf");
        }
    }

    public FloppyRedir(boolean z) {
        this.physicalDevice = z;
        this.protocol = new FloppyProtocol();
        this.packetReadBuffer = ByteBuffer.allocateDirect(131133);
        this.packetWriteBuffer = ByteBuffer.allocateDirect(131133);
    }

    private void floppyConnect(String str) throws IOException {
        this.packetMaster = new PacketMaster(str, PORT, false, this.protocol, false);
        this.packetMaster.setupBuffers(this.packetReadBuffer, this.packetWriteBuffer);
        this.packetMaster.setBufferEndianness(ByteOrder.LITTLE_ENDIAN, ByteOrder.LITTLE_ENDIAN);
        this.packetMaster.connect();
    }

    private void floppyDisconnect() {
        try {
            this.packetMaster.close();
        } catch (IOException e) {
            System.err.println("Error closing floppy connection: " + e.getMessage());
        }
    }

    public boolean startRedirection(String str, String str2) throws RedirectionException {
        if (this.running) {
            return true;
        }
        try {
            floppyConnect(str);
            IUSBSCSI recvRequest = recvRequest();
            if (recvRequest.opcode != DEVICE_REDIRECTION_ACK) {
                floppyDisconnect();
                throw new RedirectionException("Got unexpected opcode: " + recvRequest.opcode);
            } else if (recvRequest.connectionStatus != 1) {
                floppyDisconnect();
                if (recvRequest.m_otherIP != null) {
                    JViewerApp.getInstance().getMainWindow().generalErrorMessage("Floppy redirection", "Floppy Redirection is already in use.\nIf the client that is doing Floppy redirection has\nlost connectivity to the card, it may take upto 3 minutes\nfor the card to discard the old connection and\naccept a new connection.\nFloppy redirection is on from client with IP : " + recvRequest.m_otherIP);
                }
                return false;
            } else {
                if (this.nativeReaderPointer == -1) {
                    newFloppyReader(this.physicalDevice);
                }
                if (!openFloppy(str2)) {
                    System.err.println("Cannot open floppy!");
                    deleteFloppyReader();
                    floppyDisconnect();
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
                System.err.println("Interrupted while joining floppy thread");
                JOptionPane.showMessageDialog((Component) null, "Interrupted while joining floppy thread");
            }
            floppyDisconnect();
            this.running = false;
            this.stopRunning = false;
            closeFloppy();
            deleteFloppyReader();
        }
        return true;
    }

    public void stopRedirectionAbnormal() {
        if (this.running) {
            this.stopRunning = true;
            floppyDisconnect();
            this.running = false;
            this.stopRunning = false;
            closeFloppy();
            deleteFloppyReader();
            if (this.physicalDevice) {
                JViewerApp.getInstance().getJVMenu().notifyFloppyRedir(false);
            } else {
                JViewerApp.getInstance().getJVMenu().notifyFloppyImageRedir(false);
            }
        }
    }

    private IUSBSCSI recvRequest() throws IOException, RedirectionException {
        return (IUSBSCSI) this.packetMaster.receivePacket(true, true);
    }

    public boolean isRedirActive() {
        return this.running;
    }

    public void run() {
        while (!this.stopRunning) {
            try {
                this.packetWriteBuffer.rewind();
                if (recvRequest() != null) {
                    this.packetWriteBuffer.limit(executeFloppySCSICmd(this.packetReadBuffer, this.packetWriteBuffer));
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
    }

    public String[] getFloppyList() {
        if (!this.physicalDevice) {
            DisplayErrorMsg("Cannot get Floppy drive list during Floppy IMAGE redirection");
            return null;
        }
        if (this.nativeReaderPointer == -1) {
            newFloppyReader(true);
        }
        return listFloppyDrives();
    }

    public String getLIBFLOPPYVersion() {
        if (this.nativeReaderPointer != -1) {
            return getVersion();
        }
        newFloppyReader(false);
        String version = getVersion();
        deleteFloppyReader();
        return version;
    }

    public void DisplayErrorMsg(String str) {
        JViewerApp.getInstance().getMainWindow().generalErrorMessage("Device Redirection", str);
    }

    public void handleError(String str) {
        DisplayErrorMsg(str);
    }

    public boolean isPhysicalDevice() {
        return this.physicalDevice;
    }
}
