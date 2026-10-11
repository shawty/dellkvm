package com.ami.iusb.protocol;

import com.ami.iusb.EncryptionException;
import com.ami.iusb.RedirectionException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public abstract class RedirPacket {
    protected boolean encrypted = false;
    protected SecretKeySpec encryptionKey;
    public RedirHeader header;
    protected Cipher packetCipher;

    public abstract void readData(ByteBuffer byteBuffer);

    public abstract void readPacket(ByteBuffer byteBuffer) throws RedirectionException;

    public abstract void writePacket(ByteBuffer byteBuffer) throws EncryptionException;

    protected RedirPacket() {
    }

    public void setEncryptionMethod(Cipher cipher, SecretKeySpec secretKeySpec) throws EncryptionException {
        try {
            this.encrypted = true;
            this.packetCipher = cipher;
            this.encryptionKey = secretKeySpec;
            cipher.init(1, secretKeySpec);
        } catch (InvalidKeyException e) {
            this.encrypted = false;
            throw new EncryptionException("Bad encryption key");
        }
    }

    /* access modifiers changed from: protected */
    public void encryptData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i) throws EncryptionException {
        try {
            this.packetCipher.init(1, this.encryptionKey);
            byteBuffer.limit(this.header.headerLen + i);
            byteBuffer2.put(byteBuffer);
            byteBuffer.limit(byteBuffer.capacity());
            this.packetCipher.doFinal(byteBuffer, byteBuffer2);
        } catch (InvalidKeyException e) {
            throw new EncryptionException("Bad encryption key");
        } catch (ShortBufferException e2) {
            throw new EncryptionException(e2.getMessage());
        } catch (IllegalBlockSizeException e3) {
            throw new EncryptionException(e3.getMessage());
        } catch (BadPaddingException e4) {
            throw new EncryptionException(e4.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void encryptData(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws EncryptionException {
        encryptData(byteBuffer, byteBuffer2, 0);
    }

    public int getPacketStatus() {
        return this.header.status;
    }
}
