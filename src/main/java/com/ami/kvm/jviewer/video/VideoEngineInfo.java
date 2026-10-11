package com.ami.kvm.jviewer.video;

public class VideoEngineInfo {
    public COMPRESS_DATA CompressData = new COMPRESS_DATA();
    public VIDEO_MODE_INFO DestinationModeInfo = new VIDEO_MODE_INFO();
    public FRAME_HEADER FrameHeader = new FRAME_HEADER();
    public INF_DATA INFData = new INF_DATA();
    public VIDEO_MODE_INFO SourceModeInfo = new VIDEO_MODE_INFO();
    public XMS_BUFFER VideoCompressBuffer = new XMS_BUFFER();
    public XMS_BUFFER VideoOutputBuffer = new XMS_BUFFER();
    public short iEngVersion;

    class XMS_BUFFER {
        public int handle;
        public int physical_address;
        public int size;
        public int virtual_address;

        XMS_BUFFER() {
        }
    }

    /* access modifiers changed from: package-private */
    public class VIDEO_MODE_INFO {
        public short ColorDepth;
        public byte ModeIndex;
        public short RefreshRate;
        public short X;
        public short Y;

        VIDEO_MODE_INFO() {
        }
    }

    /* access modifiers changed from: package-private */
    public class FRAME_HEADER {
        public byte AdvanceScaleFactor;
        public byte AdvanceTableSelector;
        public byte CompressionMode;
        public int FrameNumber;
        public short HSize;
        public byte JPEGScaleFactor;
        public byte JPEGTableSelector;
        public byte JPEGYUVTableMapping;
        public byte Mode420;
        public int NumberOfMB;
        public byte RC4Enable;
        public byte RC4Reset;
        public int[] Reserved = {0, 0};
        public byte SharpModeSelection;
        public int StartCode;
        public short VSize;

        FRAME_HEADER() {
        }
    }

    /* access modifiers changed from: package-private */
    public class INF_DATA {
        public short AnalogDifferentialThreshold;
        public byte AutoMode;
        public byte DifferentialSetting;
        public short DigitalDifferentialThreshold;
        public byte DownScalingMethod;
        public byte ExternalSignalEnable;
        public byte VQMode;

        INF_DATA() {
        }
    }

    /* access modifiers changed from: package-private */
    public class COMPRESS_DATA {
        public int CompressSize;
        public int HDebug;
        public int SourceFrameSize;
        public int VDebug;

        COMPRESS_DATA() {
        }
    }
}
