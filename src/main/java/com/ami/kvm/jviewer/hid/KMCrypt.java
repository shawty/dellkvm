package com.ami.kvm.jviewer.hid;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class KMCrypt {
    public static final int DECRYPT = 2;
    public static final int ENCRYPT = 1;
    public static final int PLACE_HOLDER_SIZE = 16;
    private Cipher m_bf = null;

    public int initialize(byte[] bArr, String str, String str2, int i) {
        byte[] createKey = createKey(bArr, str, str2);
        if (createKey == null) {
            return -1;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(createKey, "Blowfish");
        try {
            this.m_bf = Cipher.getInstance("Blowfish/ECB/NoPadding");
            this.m_bf.init(i, secretKeySpec);
            Debug.out.println("Encryption is initialized in wrong function\n");
            return 0;
        } catch (Exception e) {
            Debug.out.println("Crypt initialization failed\n");
            return -1;
        }
    }

    public int initialize(byte[] bArr, int i) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "Blowfish");
        try {
            this.m_bf = Cipher.getInstance("Blowfish/ECB/NoPadding");
            this.m_bf.init(i, secretKeySpec);
            Debug.out.println("Encryption is initialized in CORRECT function\n");
            return 0;
        } catch (Exception e) {
            Debug.out.println("Crypt initialization failed\n");
            return -1;
        }
    }

    public byte[] encrypt(byte[] bArr) {
        try {
            return this.m_bf.doFinal(pad(bArr));
        } catch (Exception e) {
            Debug.out.println("Encryption failed\n");
            return null;
        }
    }

    public byte[] decrypt(byte[] bArr) {
        try {
            return this.m_bf.doFinal(bArr);
        } catch (Exception e) {
            Debug.out.println("Decryption failed\n");
            return null;
        }
    }

    public void close() {
        this.m_bf = null;
    }

    public boolean isInitialized() {
        if (this.m_bf == null) {
            return false;
        }
        return true;
    }

    private byte[] createKey(byte[] bArr, String str, String str2) {
        new String();
        try {
            byte[] address = InetAddress.getLocalHost().getAddress();
            byte[] bytes = (Integer.toString(ubyte(address[0])) + "." + Integer.toString(ubyte(address[1])) + "." + Integer.toString(ubyte(address[2])) + "." + Integer.toString(ubyte(address[3]))).getBytes();
            byte[] bytes2 = str.getBytes();
            byte[] bytes3 = str2.getBytes();
            byte[] bytes4 = ":".getBytes();
            byte[] bArr2 = new byte[(bytes.length + bytes4.length + bytes2.length + bytes4.length + 16 + bytes4.length + bytes3.length)];
            System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
            int length = bytes.length + 0;
            System.arraycopy(bytes4, 0, bArr2, length, bytes4.length);
            int length2 = length + bytes4.length;
            System.arraycopy(bytes2, 0, bArr2, length2, bytes2.length);
            int length3 = length2 + bytes2.length;
            System.arraycopy(bytes4, 0, bArr2, length3, bytes4.length);
            int length4 = length3 + bytes4.length;
            System.arraycopy(bArr, 0, bArr2, length4, 16);
            int i = length4 + 16;
            System.arraycopy(bytes4, 0, bArr2, i, bytes4.length);
            int length5 = i + bytes4.length;
            System.arraycopy(bytes3, 0, bArr2, length5, bytes3.length);
            int length6 = length5 + bytes3.length;
            try {
                return MessageDigest.getInstance("MD5").digest(bArr2);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Failed to create the key");
                return null;
            }
        } catch (Exception e2) {
            System.out.println("Failed to get local IP");
            return null;
        }
    }

    private int ubyte(byte b) {
        int i;
        if ((b & 128) != 0) {
            i = USBMessage.IUSB_FROM_REMOTE;
        } else {
            i = 0;
        }
        return i + (b & Byte.MAX_VALUE);
    }

    private byte[] pad(byte[] bArr) {
        if (bArr.length % 8 == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[(bArr.length + (8 - (bArr.length % 8)))];
        Arrays.fill(bArr2, (byte) 0);
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }
}
