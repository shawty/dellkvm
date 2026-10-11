package com.ami.iusb.protocol;

import com.ami.iusb.RedirProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public abstract class RedirProtocol {
    public abstract int getDataLen(ByteBuffer byteBuffer) throws RedirProtocolException;

    public abstract RedirPacket getPacket(ByteBuffer byteBuffer) throws RedirProtocolException, BufferUnderflowException;

    protected RedirProtocol() {
    }
}
