package com.ami.iusb.protocol;

import com.ami.iusb.RedirProtocolException;
import java.nio.ByteBuffer;

public abstract class RedirHeader {
    public int command;
    public long dataLen;
    public int headerLen;
    public int serverID;
    public int serverVer;
    public int status;

    public abstract void read(ByteBuffer byteBuffer) throws RedirProtocolException;

    public abstract void write(ByteBuffer byteBuffer);
}
