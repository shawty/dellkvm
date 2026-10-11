package com.ami.kvm.jviewer.video;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class FrameHdr {
    public static final int HDR_SIZE = 19;
    private ASP2000ImgHdr m_aihdr = new ASP2000ImgHdr();
    private VideoEngineInfo m_myVEI = new VideoEngineInfo();
    private VideoHdr m_vidhdr = new VideoHdr();

    public byte[] set(ByteBuffer byteBuffer) {
        return this.m_aihdr.setASPVariables(ByteBuffer.wrap(this.m_vidhdr.set(byteBuffer)));
    }

    public boolean IsValidSignature() {
        byte[] bytes = "ASP-2000".getBytes();
        VideoHdr videoHdr = this.m_vidhdr;
        if (Arrays.equals(bytes, VideoHdr.m_bSignature)) {
            return true;
        }
        return false;
    }

    public VideoEngineInfo assignASPVariables() {
        this.m_myVEI.SourceModeInfo.X = this.m_aihdr.SourceMode_X;
        this.m_myVEI.SourceModeInfo.Y = this.m_aihdr.SourceMode_Y;
        this.m_myVEI.DestinationModeInfo.X = this.m_aihdr.DestinationMode_X;
        this.m_myVEI.DestinationModeInfo.Y = this.m_aihdr.DestinationMode_Y;
        this.m_myVEI.CompressData.CompressSize = this.m_aihdr.CompressData_CompressSize;
        this.m_myVEI.FrameHeader.RC4Enable = this.m_aihdr.FrameHdr_RC4Enable;
        this.m_myVEI.FrameHeader.RC4Reset = this.m_aihdr.FrameHdr_RC4Reset;
        this.m_myVEI.FrameHeader.Mode420 = this.m_aihdr.Mode420;
        this.m_myVEI.FrameHeader.JPEGScaleFactor = this.m_aihdr.FrameHdr_JPEGScaleFactor;
        this.m_myVEI.FrameHeader.NumberOfMB = this.m_aihdr.FrameHdr_NumberOfMB;
        this.m_myVEI.FrameHeader.AdvanceScaleFactor = this.m_aihdr.FrameHdr_AdvanceScaleFactor;
        this.m_myVEI.FrameHeader.JPEGTableSelector = this.m_aihdr.FrameHdr_JPEGTableSelector;
        this.m_myVEI.FrameHeader.AdvanceTableSelector = this.m_aihdr.FrameHdr_AdvanceTableSelector;
        this.m_myVEI.FrameHeader.JPEGYUVTableMapping = this.m_aihdr.FrameHdr_JPEGYUVTableMapping;
        this.m_myVEI.FrameHeader.SharpModeSelection = this.m_aihdr.FrameHdr_SharpModeSelection;
        this.m_myVEI.FrameHeader.CompressionMode = this.m_aihdr.FrameHdr_CompressionMode;
        this.m_myVEI.INFData.DownScalingMethod = this.m_aihdr.InfData_DownScalingMethod;
        this.m_myVEI.INFData.DifferentialSetting = this.m_aihdr.InfData_DifferentialSetting;
        this.m_myVEI.INFData.AnalogDifferentialThreshold = this.m_aihdr.InfData_AnalogDifferentialThreshold;
        this.m_myVEI.INFData.DigitalDifferentialThreshold = this.m_aihdr.InfData_DigitalDifferentialThreshold;
        this.m_myVEI.INFData.AutoMode = this.m_aihdr.InfData_AutoMode;
        this.m_myVEI.INFData.VQMode = this.m_aihdr.InfData_VQMode;
        return this.m_myVEI;
    }
}
