package com.ami.iusb.protocol;

import com.ami.iusb.RedirectionException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class PacketMaster {
    private static final int IUSB_HDR_SIZE = 32;
    private static final int RECEIVE_TIMEOUT = 60;
    private ByteBuffer appIn;
    private ByteBuffer appOut;
    private boolean blocking;
    private boolean debug = false;
    private SSLEngine engine;
    private String host;
    private ByteBuffer netIn;
    private ByteBuffer netOut;
    private Selector packetSel;
    private SocketChannel packetSock;
    private int port;
    private RedirProtocol protocol;
    private SSLSession session;
    private boolean sslEncryption;
    private boolean wakeup = false;

    public PacketMaster(String str, int i, boolean z, RedirProtocol redirProtocol, boolean z2) {
        this.host = str;
        this.port = i;
        this.blocking = z;
        this.protocol = redirProtocol;
        this.sslEncryption = z2;
        if (z2) {
            this.session = this.engine.getSession();
        }
    }

    public void setupBuffers(int i, int i2) {
        if (this.sslEncryption) {
            this.appIn = ByteBuffer.allocateDirect(i);
            this.appOut = ByteBuffer.allocateDirect(i2);
            this.netIn = ByteBuffer.allocateDirect(this.session.getPacketBufferSize());
            this.netOut = ByteBuffer.allocateDirect(this.session.getPacketBufferSize());
            debugOut("Allocating SSL network buffers and application buffers");
            debugOut("Allocated netOut buffer of " + this.netOut.capacity() + " bytes");
            debugOut("Allocated appOut buffer of " + this.appOut.capacity() + " bytes");
            debugOut("Allocated netIn buffer of " + this.netIn.capacity() + " bytes");
            debugOut("Allocated appIn buffer of " + this.appIn.capacity() + " bytes");
            return;
        }
        this.netIn = ByteBuffer.allocateDirect(i);
        this.netOut = ByteBuffer.allocateDirect(i2);
        this.appIn = this.netIn;
        this.appOut = this.netOut;
        debugOut("No SSL, allocating only netIn and netOut...");
        debugOut("Allocated netOut buffer of " + this.netOut.capacity() + " bytes");
        debugOut("Allocated netIn buffer of " + this.netIn.capacity() + " bytes");
    }

    public void setupBuffers(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (this.sslEncryption) {
            this.appIn = byteBuffer;
            this.appOut = byteBuffer2;
            this.netIn = ByteBuffer.allocateDirect(this.session.getPacketBufferSize());
            this.netOut = ByteBuffer.allocateDirect(this.session.getPacketBufferSize());
            this.appIn.clear();
            this.appOut.clear();
            debugOut("Using SSL, allocating netIn and netOut buffers");
            debugOut("Using provided appIn and appOut buffers");
            return;
        }
        this.netIn = byteBuffer;
        this.netOut = byteBuffer2;
        this.appIn = this.netIn;
        this.appOut = this.netOut;
        this.netIn.clear();
        this.netOut.clear();
        debugOut("No SSL, using provided buffers exclusively");
    }

    public void setBufferEndianness(ByteOrder byteOrder, ByteOrder byteOrder2) {
        this.netIn.order(byteOrder);
        this.netOut.order(byteOrder2);
    }

    public void connect() throws IOException {
        try {
            this.packetSock = SocketChannel.open(new InetSocketAddress(this.host, this.port));
            if (!this.blocking) {
                this.packetSock.configureBlocking(false);
            }
            this.packetSel = Selector.open();
            this.packetSock.register(this.packetSel, 1);
            if (this.sslEncryption) {
                initSSL();
            }
        } catch (UnresolvedAddressException e) {
            throw new IOException("Cannot resolve host: " + this.host);
        }
    }

    private void initSSL() throws IOException {
        SSLContext sSLContext;
        try {
            KeyStore instance = KeyStore.getInstance("JKS");
            instance.load(new FileInputStream("/home/andrewm/stunnelstore"), "trustword".toCharArray());
            TrustManagerFactory instance2 = TrustManagerFactory.getInstance("SunX509");
            instance2.init(instance);
            sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, instance2.getTrustManagers(), null);
        } catch (KeyStoreException e) {
            System.err.println("Bad keystore: " + e.getMessage());
            sSLContext = null;
        } catch (CertificateException e2) {
            System.err.println("Bad certificate: " + e2.getMessage());
            sSLContext = null;
        } catch (NoSuchAlgorithmException e3) {
            System.err.println("No such algorithm: " + e3.getMessage());
            sSLContext = null;
        } catch (KeyManagementException e4) {
            System.err.println("Key management exception: " + e4.getMessage());
            sSLContext = null;
        }
        this.engine = sSLContext.createSSLEngine();
        this.engine.setUseClientMode(true);
        try {
            this.engine.beginHandshake();
            while (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                if (this.engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                    this.netOut.clear();
                    debugOut("Wrapping: " + this.engine.wrap(this.appOut, this.netOut));
                    this.netOut.flip();
                    debugOut("Wrote " + this.packetSock.write(this.netOut) + " bytes");
                } else if (this.engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                    if (this.netIn.position() == 0) {
                        System.out.println("Read " + this.packetSock.read(this.netIn) + " bytes");
                    }
                    this.netIn.flip();
                    System.out.println("Unwrapping: " + this.engine.unwrap(this.netIn, this.appIn));
                    this.netIn.compact();
                    debugOut("netIn.position(): " + this.netIn.position());
                } else if (this.engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    while (true) {
                        Runnable delegatedTask = this.engine.getDelegatedTask();
                        if (delegatedTask == null) {
                            break;
                        }
                        delegatedTask.run();
                    }
                    System.out.println(this.engine.getHandshakeStatus());
                }
            }
        } catch (SSLException e5) {
            System.err.println("Got SSLException: " + e5.getMessage());
        }
        debugOut("Handshaking is complete!");
    }

    public void close() throws IOException {
        this.packetSock.close();
        this.packetSel.close();
    }

    public void wakeup() {
        this.wakeup = true;
        this.packetSel.wakeup();
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendPacket(com.ami.iusb.protocol.RedirPacket r4) throws java.io.IOException, com.ami.iusb.EncryptionException {
        /*
            r3 = this;
            java.nio.ByteBuffer r0 = r3.appOut
            r0.clear()
            java.nio.ByteBuffer r0 = r3.netOut
            r0.clear()
            java.nio.ByteBuffer r0 = r3.appOut
            r4.writePacket(r0)
            java.nio.ByteBuffer r0 = r3.appOut
            r0.flip()
            javax.net.ssl.SSLEngine r0 = r3.engine
            if (r0 == 0) goto L_0x0026
            javax.net.ssl.SSLEngine r0 = r3.engine
            java.nio.ByteBuffer r1 = r3.appOut
            java.nio.ByteBuffer r2 = r3.netOut
            r0.wrap(r1, r2)
            java.nio.ByteBuffer r0 = r3.netOut
            r0.flip()
        L_0x0026:
            java.nio.ByteBuffer r0 = r3.netOut
            boolean r0 = r0.hasRemaining()
            if (r0 == 0) goto L_0x0039
            java.nio.channels.SocketChannel r0 = r3.packetSock
            java.nio.ByteBuffer r1 = r3.netOut
            int r0 = r0.write(r1)
            r1 = -1
            if (r0 != r1) goto L_0x0026
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ami.iusb.protocol.PacketMaster.sendPacket(com.ami.iusb.protocol.RedirPacket):void");
    }

    public RedirPacket receivePacket(boolean z) throws IOException, RedirectionException {
        RedirPacket redirPacket;
        if (this.netIn.position() != 0) {
            this.netIn.flip();
            redirPacket = this.protocol.getPacket(this.appIn);
            if (redirPacket == null) {
                this.netIn.position(this.netIn.limit());
                this.netIn.limit(this.netIn.capacity());
            } else {
                debugOut("Got a packet!");
            }
        } else {
            redirPacket = null;
        }
        while (redirPacket == null) {
            int select = this.packetSel.select(60000);
            if (this.wakeup) {
                this.wakeup = false;
                return null;
            } else if (select > 0) {
                Iterator<SelectionKey> it = this.packetSel.selectedKeys().iterator();
                it.next();
                it.remove();
                this.packetSock.read(this.netIn);
                debugOut("Read " + this.netIn.position() + " bytes");
                this.netIn.flip();
                if (this.engine != null) {
                    if (this.engine.unwrap(this.netIn, this.appIn).getStatus() == SSLEngineResult.Status.OK) {
                        this.appIn.flip();
                    }
                    this.netIn.compact();
                }
                redirPacket = this.protocol.getPacket(this.appIn);
                if (redirPacket == null) {
                    debugOut("Can't build a packet from input data");
                    this.netIn.position(this.netIn.limit());
                    this.netIn.limit(this.netIn.capacity());
                } else {
                    debugOut("Got a packet!");
                }
            } else if (!isConnectionAlive()) {
                close();
                throw new RedirectionException("Timed out while receiving data");
            }
        }
        if (!z) {
            return redirPacket;
        }
        this.appIn.compact();
        return redirPacket;
    }

    public RedirPacket receivePacket(boolean z, boolean z2) throws IOException, RedirectionException {
        int i;
        int i2;
        this.netIn.limit(32);
        this.netIn.position(0);
        int i3 = 0;
        while (i3 < 32) {
            int select = this.packetSel.select(60000);
            if (this.wakeup) {
                this.wakeup = false;
                debugOut("(Header ) wakeup is true ");
                return null;
            } else if (select > 0) {
                Iterator<SelectionKey> it = this.packetSel.selectedKeys().iterator();
                it.next();
                it.remove();
                int read = this.packetSock.read(this.netIn);
                debugOut("Read " + read + " bytes");
                if (read > 0) {
                    i2 = read + i3;
                } else {
                    i2 = i3;
                }
                i3 = i2;
            } else if (!isConnectionAlive()) {
                close();
                throw new RedirectionException("Timed out while receiving data");
            }
        }
        this.netIn.position(0);
        int dataLen = this.protocol.getDataLen(this.appIn);
        if (dataLen < 0) {
            debugOut("Data size is less than zero");
            return null;
        }
        if (dataLen > 0) {
            this.netIn.limit(dataLen + 32);
            this.netIn.position(32);
            int i4 = 0;
            while (i4 < dataLen) {
                int select2 = this.packetSel.select(60000);
                if (this.wakeup) {
                    this.wakeup = false;
                    debugOut("(Data) Wakeup is true");
                    return null;
                } else if (select2 > 0) {
                    Iterator<SelectionKey> it2 = this.packetSel.selectedKeys().iterator();
                    it2.next();
                    it2.remove();
                    int read2 = this.packetSock.read(this.netIn);
                    debugOut("Read " + read2 + " bytes");
                    if (read2 > 0) {
                        i = read2 + i4;
                    } else {
                        i = i4;
                    }
                    i4 = i;
                } else if (!isConnectionAlive()) {
                    close();
                    throw new RedirectionException("Timed out while receiving data");
                }
            }
        }
        this.netIn.flip();
        if (this.engine != null) {
            if (this.engine.unwrap(this.netIn, this.appIn).getStatus() == SSLEngineResult.Status.OK) {
                this.appIn.flip();
            }
            this.netIn.compact();
        }
        RedirPacket packet = this.protocol.getPacket(this.appIn);
        if (packet == null) {
            debugOut("Can't build a packet from input data");
            return null;
        }
        debugOut("Got a packet!");
        if (!z) {
            return packet;
        }
        this.appIn.compact();
        return packet;
    }

    public RedirPacket receivePacket() throws IOException, RedirectionException {
        return receivePacket(true);
    }

    public void clearBuffer() {
        this.appIn.compact();
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }

    private boolean isConnectionAlive() {
        boolean z = true;
        SocketChannel socketChannel = null;
        try {
            SocketChannel open = SocketChannel.open(new InetSocketAddress(this.host, this.port));
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    System.err.println("Error closing test socket: " + e.getMessage());
                }
            }
        } catch (IOException e2) {
            z = false;
            if (0 != 0) {
                try {
                    socketChannel.close();
                } catch (IOException e3) {
                    System.err.println("Error closing test socket: " + e3.getMessage());
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    socketChannel.close();
                } catch (IOException e4) {
                    System.err.println("Error closing test socket: " + e4.getMessage());
                }
            }
            throw th;
        }
        return z;
    }

    private void debugOut(String str) {
        if (this.debug) {
            System.out.println(str);
        }
    }
}
