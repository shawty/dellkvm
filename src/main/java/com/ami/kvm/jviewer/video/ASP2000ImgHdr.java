package com.ami.kvm.jviewer.video;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ASP2000ImgHdr {
    public static final short ASP2000_IMG_HDR_SIZE = 84;
    int CompressData_CompressSize;
    int CompressData_HDebug;
    int CompressData_SourceFrameSize;
    int CompressData_VDebug;
    short Cursor_XPos;
    short Cursor_YPos;
    short DestinationMode_ColorDepth;
    byte DestinationMode_ModeIndex;
    short DestinationMode_RefreshRate;
    short DestinationMode_X;
    short DestinationMode_Y;
    byte FrameHdr_AdvanceScaleFactor;
    byte FrameHdr_AdvanceTableSelector;
    byte FrameHdr_CompressionMode;
    int FrameHdr_FrameNumber;
    short FrameHdr_HSize;
    byte FrameHdr_JPEGScaleFactor;
    byte FrameHdr_JPEGTableSelector;
    byte FrameHdr_JPEGYUVTableMapping;
    int FrameHdr_NumberOfMB;
    byte FrameHdr_RC4Enable;
    byte FrameHdr_RC4Reset;
    int[] FrameHdr_Reserved = new int[2];
    byte FrameHdr_SharpModeSelection;
    int FrameHdr_StartCode;
    short FrameHdr_VSize;
    short InfData_AnalogDifferentialThreshold;
    byte InfData_AutoMode;
    byte InfData_DifferentialSetting;
    short InfData_DigitalDifferentialThreshold;
    byte InfData_DownScalingMethod;
    byte InfData_ExternalSignalEnable;
    byte InfData_VQMode;
    byte InputSignal;
    byte Mode420;
    short SourceMode_ColorDepth;
    byte SourceMode_ModeIndex;
    short SourceMode_RefreshRate;
    short SourceMode_X;
    short SourceMode_Y;
    short iEngVersion;
    short wHeaderLen;

    public void ASP2000ImgHdr() {
    }

    public byte[] setASPVariables(ByteBuffer byteBuffer) {
        int limit = byteBuffer.limit();
        if (limit > 86) {
            limit -= 86;
        }
        byte[] bArr = new byte[limit];
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.iEngVersion = byteBuffer.getShort();
        this.wHeaderLen = byteBuffer.getShort();
        this.SourceMode_X = byteBuffer.getShort();
        this.SourceMode_Y = byteBuffer.getShort();
        this.SourceMode_ColorDepth = byteBuffer.getShort();
        this.SourceMode_RefreshRate = byteBuffer.getShort();
        this.SourceMode_ModeIndex = byteBuffer.get();
        this.DestinationMode_X = byteBuffer.getShort();
        this.DestinationMode_Y = byteBuffer.getShort();
        this.DestinationMode_ColorDepth = byteBuffer.getShort();
        this.DestinationMode_RefreshRate = byteBuffer.getShort();
        this.DestinationMode_ModeIndex = byteBuffer.get();
        this.FrameHdr_StartCode = byteBuffer.getInt();
        this.FrameHdr_FrameNumber = byteBuffer.getInt();
        this.FrameHdr_HSize = byteBuffer.getShort();
        this.FrameHdr_VSize = byteBuffer.getShort();
        this.FrameHdr_Reserved[0] = byteBuffer.getInt();
        this.FrameHdr_Reserved[1] = byteBuffer.getInt();
        this.FrameHdr_CompressionMode = byteBuffer.get();
        this.FrameHdr_JPEGScaleFactor = byteBuffer.get();
        this.FrameHdr_JPEGTableSelector = byteBuffer.get();
        this.FrameHdr_JPEGYUVTableMapping = byteBuffer.get();
        this.FrameHdr_SharpModeSelection = byteBuffer.get();
        this.FrameHdr_AdvanceTableSelector = byteBuffer.get();
        this.FrameHdr_AdvanceScaleFactor = byteBuffer.get();
        this.FrameHdr_NumberOfMB = byteBuffer.getInt();
        this.FrameHdr_RC4Enable = byteBuffer.get();
        this.FrameHdr_RC4Reset = byteBuffer.get();
        this.Mode420 = byteBuffer.get();
        this.InfData_DownScalingMethod = byteBuffer.get();
        this.InfData_DifferentialSetting = byteBuffer.get();
        this.InfData_AnalogDifferentialThreshold = byteBuffer.getShort();
        this.InfData_DigitalDifferentialThreshold = byteBuffer.getShort();
        this.InfData_ExternalSignalEnable = byteBuffer.get();
        this.InfData_AutoMode = byteBuffer.get();
        this.InfData_VQMode = byteBuffer.get();
        this.CompressData_SourceFrameSize = byteBuffer.getInt();
        this.CompressData_CompressSize = byteBuffer.getInt();
        this.CompressData_HDebug = byteBuffer.getInt();
        this.CompressData_VDebug = byteBuffer.getInt();
        this.InputSignal = byteBuffer.get();
        this.Cursor_XPos = byteBuffer.getShort();
        this.Cursor_YPos = byteBuffer.getShort();
        try {
            byteBuffer.get(bArr);
        } catch (Exception e) {
            System.out.println(" EXCEPTION ");
            e.printStackTrace();
        }
        return bArr;
    }
}
