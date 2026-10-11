package com.ami.kvm.jviewer.video;

import com.ami.kvm.jviewer.gui.JViewerView;
import com.ami.kvm.jviewer.gui.VideoSettings;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.kvmpkts.USBMessage;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Decoder {
    static short[] DCCb = new short[1];
    static short[] DCCr = new short[1];
    public static final int MAX_X_RESOLUTION = 1500;
    public static final int MAX_Y_RESOLUTION = 1500;
    static short[] m_DCY = new short[1];
    static int m_newbits;
    int ADVANCESCALEFACTOR;
    int ADVANCESCALEFACTORUV;
    public final int BLOCK_AST2100_SKIP_LENGTH = 20;
    public final int BLOCK_AST2100_START_LENGTH = 4;
    public final int BLOCK_HEADER_MASK = 15;
    public final int BLOCK_HEADER_S_MASK = 1;
    public final int BLOCK_START_LENGTH = 2;
    public final int BLOCK_START_MASK = 3;
    byte[] Cb = new byte[64];
    byte CbH;
    byte CbQ_nr;
    byte CbV;
    byte[] Cr = new byte[64];
    byte CrH;
    byte CrQ_nr;
    byte CrV;
    final int DCTSIZE = 8;
    final int DCTSIZE_0 = 0;
    final int DCTSIZE_1 = 8;
    final int DCTSIZE_2 = 16;
    final int DCTSIZE_3 = 24;
    final int DCTSIZE_4 = 32;
    final int DCTSIZE_5 = 40;
    final int DCTSIZE_6 = 48;
    final int DCTSIZE_7 = 56;
    short[] DCT_tcoeff = new short[64];
    byte[] DecodeKeys = "fedcba9876543210".getBytes();
    boolean DecodeRC4State;
    final int FIX_1_082392200 = 277;
    final int FIX_1_414213562 = 362;
    final int FIX_1_847759065 = 473;
    final int FIX_2_613125930 = 669;
    public final int FRAME_END_CODE = 9;
    int HEIGHT;
    public final int JPEG_BLOCK_SKIP_CODE = 3;
    public final int JPEG_BLOCK_START_CODE = 1;
    public final int JPEG_NO_SKIP_CODE = 0;
    public final int JPEG_SKIP_CODE = 8;
    public final int LEFT_LEAF = 1;
    final int LENGTH_256 = 256;
    public final int LOW_JPEG_NO_SKIP_CODE = 4;
    public final int LOW_JPEG_SKIP_CODE = 12;
    int Mapping;
    public final int NO_LEAF = 0;
    final int POWEROFTOW = 17;
    public final int RIGHT_LEAF = 2;
    final int RTABLE_LENGTH = 1408;
    int RealHEIGHT;
    int RealWIDTH;
    int SCALEFACTOR;
    int SCALEFACTORUV;
    int SharpModeSelection;
    public final int VQ_BLOCK_SKIP_CODE = 2;
    public final int VQ_BLOCK_START_CODE = 0;
    public final int VQ_COLOR_MASK = 16777215;
    public final int VQ_HEADER_MASK = 1;
    public final int VQ_INDEX_MASK = 3;
    public final int VQ_NO_SKIP_1_COLOR_CODE = 5;
    public final int VQ_NO_SKIP_2_COLOR_CODE = 6;
    public final int VQ_NO_SKIP_4_COLOR_CODE = 7;
    public final int VQ_NO_UPDATE_HEADER = 0;
    public final int VQ_NO_UPDATE_LENGTH = 3;
    public final int VQ_SKIP_1_COLOR_CODE = 13;
    public final int VQ_SKIP_2_COLOR_CODE = 14;
    public final int VQ_SKIP_4_COLOR_CODE = 15;
    public final int VQ_UPDATE_HEADER = 1;
    public final int VQ_UPDATE_LENGTH = 27;
    int WIDTH;
    byte[] Y = new byte[64];
    byte YH;
    byte YQ_nr;
    byte YV;
    int _index;
    int advance_selector;
    RGB1[] bitmap;
    byte bp;
    byte[] buf;
    int byte_pos;
    int codesize;
    byte d_k;
    short[] huff_values;
    short lookBitResult;
    byte m_CbAC_nr;
    byte m_CbDC_nr;
    int[] m_CbToB = new int[256];
    int[] m_CbToG = new int[256];
    short[] m_Cb_tab = new short[256];
    byte m_CrAC_nr;
    byte m_CrDC_nr;
    int[] m_CrToG = new int[256];
    int[] m_CrToR = new int[256];
    short[] m_Cr_Cb_green_tab = new short[CfgBandwidth.BANDWIDTH_512KBPS];
    short[] m_Cr_tab = new short[256];
    int[] m_DCT_coeff = new int[384];
    Huffman_table[] m_HTAC = new Huffman_table[4];
    Huffman_table[] m_HTDC = new Huffman_table[4];
    byte m_Mode420 = 1;
    long[][] m_QT;
    int[] m_RecvBuffer;
    COLOR_CACHE m_VQ = new COLOR_CACHE();
    int[] m_Y = new int[256];
    byte m_YAC_nr;
    byte m_YDC_nr;
    private byte[] m_alovicaBuf;
    int[] m_byTileYuv = new int[768];
    int m_codebuf;
    int[] m_mask = new int[17];
    int m_newbuf;
    byte[] m_rlimit_table = new byte[1408];
    short[] m_rlimit_table_short = new short[1408];
    private JVVideo m_vidClnt;
    private JViewerView m_view;
    short[] maj_code;
    short[] min_code;
    final int nBlocksInMcu = 6;
    int nZeroACTerms = 0;
    short[] neg_pow2 = new short[17];
    int oldxb;
    int oldyb;
    int[] pcb = new int[64];
    int[] pcr = new int[64];
    long pixels;
    int position;
    int[] py = new int[768];
    int[][] py420 = ((int[][]) Array.newInstance(Integer.TYPE, 4, 64));
    int[] rc4Tmp = new int[256];
    int readbuf;
    rc4_state s;
    int selector;
    short signed_wordvalue;
    byte[] tab_1 = new byte[64];
    byte[] tab_2 = new byte[64];
    byte[] tab_3 = new byte[64];
    byte[] tab_4 = new byte[64];
    int tmp_HEIGHTBy16;
    int tmp_WIDTHBy16;
    int txb;
    int tyb;
    long uprdbuf_readbuf;
    COMPRESSHEADER uvheader;
    short w1;
    short w2;
    int wordval;
    int[] workspace = new int[64];
    COMPRESSHEADER yheader;

    class RGB1 {
        byte B;
        byte G;
        byte R;

        public RGB1() {
        }
    }

    class YUV {
        byte U = 0;
        byte V = 0;
        byte Y = 0;

        public YUV() {
        }
    }

    /* access modifiers changed from: package-private */
    public class COMPRESSHEADER {
        int bheight;
        int bwidth;
        int cmptype;
        int codesize;
        int height;
        int modes;
        int nd0;
        int nd1;
        int nd11;
        int nd12;
        int nd13;
        int nd14;
        int nd15;
        int qfactor;
        int qfactoruv;
        int width;

        COMPRESSHEADER() {
        }
    }

    /* access modifiers changed from: package-private */
    public class COLOR_CACHE {
        byte BitMapBits;
        long[] Color = new long[4];
        char[] Index = new char[4];

        COLOR_CACHE() {
        }
    }

    class YUV444 {
        byte U = 0;
        byte V = 0;
        byte Y = 0;

        public YUV444() {
        }
    }

    class YUV422 {
        byte U = 0;
        byte V = 0;
        byte Y0 = 0;
        byte Y1 = 0;

        public YUV422() {
        }
    }

    /* access modifiers changed from: package-private */
    public class rc4_state {
        int[] m = new int[256];
        int x;
        int y;

        rc4_state() {
        }
    }

    /* access modifiers changed from: package-private */
    public final short WORD_hi_lo(byte b, byte b2) {
        return (short) ((getShort(b) << 8) | getShort(b2));
    }

    /* access modifiers changed from: package-private */
    public final long getLong(int i) {
        return ((long) i) & 4294967295L;
    }

    /* access modifiers changed from: package-private */
    public final int getInt(int i) {
        return 65535 & i;
    }

    /* access modifiers changed from: package-private */
    public final short getShort(byte b) {
        return (short) (b & 255);
    }

    public Decoder(JViewerView jViewerView, byte[] bArr, JVVideo jVVideo) {
        this.m_view = jViewerView;
        this.m_alovicaBuf = bArr;
        this.m_vidClnt = jVVideo;
        this.position = 0;
        this.DecodeRC4State = false;
        this.m_YDC_nr = 0;
        this.m_CbDC_nr = 1;
        this.m_CrDC_nr = 1;
        this.m_YAC_nr = 0;
        this.m_CbAC_nr = 1;
        this.m_CrAC_nr = 1;
        this.d_k = 0;
        for (int i = 1; i < 17; i++) {
            this.neg_pow2[i] = (short) ((int) (1.0d - Math.pow(2.0d, (double) i)));
        }
        for (int i2 = 0; i2 < 4; i2++) {
            this.m_HTDC[i2] = new Huffman_table();
            this.m_HTAC[i2] = new Huffman_table();
        }
        this.uvheader = new COMPRESSHEADER();
        this.yheader = new COMPRESSHEADER();
        this.w1 = 0;
        this.w2 = 0;
        this.bp = 0;
        this.wordval = 0;
        init_jpg_table();
    }

    public final void setBuf(byte[] bArr) {
        this.m_alovicaBuf = bArr;
    }

    /* access modifiers changed from: package-private */
    public final void prepare_range_limit_table() {
        Arrays.fill(this.m_rlimit_table, 0, (int) VideoSettings.GAIN_MAX, (byte) 0);
        for (short s2 = 0; s2 < 256; s2 = (short) (s2 + 1)) {
            this.m_rlimit_table[s2 + 256] = (byte) s2;
            this.m_rlimit_table_short[s2 + 256] = s2;
        }
        Arrays.fill(this.m_rlimit_table, 512, 895, (byte) -1);
        Arrays.fill(this.m_rlimit_table_short, 512, 895, (short) IVTPPktHdr.ENCRYPTION_ENABLED);
        Arrays.fill(this.m_rlimit_table, 896, 1279, (byte) 0);
        Arrays.fill(this.m_rlimit_table_short, 896, 1279, (short) 0);
        for (short s3 = 1024; s3 < 1152; s3 = (short) (s3 + 1)) {
            this.m_rlimit_table[s3 + 256] = (byte) s3;
            this.m_rlimit_table_short[s3 + 256] = (short) (s3 & IVTPPktHdr.ENCRYPTION_ENABLED);
        }
    }

    /* access modifiers changed from: package-private */
    public final short lookKbits(byte b) {
        return (short) ((int) (getLong(this.m_RecvBuffer[0]) >> (32 - b)));
    }

    /* access modifiers changed from: package-private */
    public final void skipKbits(byte b) {
        if (m_newbits - b <= 0) {
            if (IVTPPktHdr.VIRTADD + this._index > this.m_RecvBuffer.length - 1) {
                this._index = (this.m_RecvBuffer.length - 1) - IVTPPktHdr.VIRTADD;
            }
            this.m_RecvBuffer[0] = ((int) (getLong(this.m_RecvBuffer[0]) << b)) | ((int) ((getLong(this.m_RecvBuffer[1]) | (getLong(this.m_RecvBuffer[IVTPPktHdr.VIRTADD + this._index]) >> m_newbits)) >> (32 - b)));
            this.m_RecvBuffer[1] = (int) (getLong(this.m_RecvBuffer[IVTPPktHdr.VIRTADD + this._index]) << (b - m_newbits));
            m_newbits = (m_newbits + 32) - b;
            this._index++;
            return;
        }
        this.m_RecvBuffer[0] = (this.m_RecvBuffer[0] << b) | ((int) (getLong(this.m_RecvBuffer[1]) >> (32 - b)));
        this.m_RecvBuffer[1] = this.m_RecvBuffer[1] << b;
        m_newbits -= b;
    }

    /* access modifiers changed from: package-private */
    public final short getKbits(byte b) {
        this.signed_wordvalue = lookKbits(b);
        if (((1 << (b - 1)) & ((long) this.signed_wordvalue)) == 0) {
            this.signed_wordvalue = (short) (this.signed_wordvalue + this.neg_pow2[b]);
        }
        skipKbits(b);
        return this.signed_wordvalue;
    }

    /* access modifiers changed from: package-private */
    public final void calculate_mask() {
        for (byte b = 0; b <= 16; b = (byte) (b + 1)) {
            this.m_mask[b] = (int) ((getLong(CfgBandwidth.BANDWIDTH_512KBPS) >> b) - 1);
        }
    }

    /* access modifiers changed from: package-private */
    public byte[] set_quant_table(byte[] bArr, byte b, byte[] bArr2) {
        for (byte b2 = 0; b2 < 64; b2 = (byte) (b2 + 1)) {
            int i = (bArr[b2] * 16) / b;
            if (i <= 0) {
                i = 1;
            }
            if (i > 255) {
                i = 255;
            }
            bArr2[JTables.zigzag[b2]] = (byte) i;
        }
        return bArr2;
    }

    /* access modifiers changed from: package-private */
    public void load_quant_table(long[] jArr) {
        float[] fArr = {1.0f, 1.3870399f, 1.306563f, 1.1758755f, 1.0f, 0.78569496f, 0.5411961f, 0.27589938f};
        byte[] bArr = new byte[64];
        switch (this.selector) {
            case 0:
                JTables.std_luminance_qt = JTables.Tbl_000Y;
                break;
            case 1:
                JTables.std_luminance_qt = JTables.Tbl_014Y;
                break;
            case 2:
                JTables.std_luminance_qt = JTables.Tbl_029Y;
                break;
            case 3:
                JTables.std_luminance_qt = JTables.Tbl_043Y;
                break;
            case 4:
                JTables.std_luminance_qt = JTables.Tbl_057Y;
                break;
            case 5:
                JTables.std_luminance_qt = JTables.Tbl_071Y;
                break;
            case 6:
                JTables.std_luminance_qt = JTables.Tbl_086Y;
                break;
            case IVTPPktHdr.HDR_SIZE:
                JTables.std_luminance_qt = JTables.Tbl_100Y;
                break;
        }
        byte[] bArr2 = set_quant_table(JTables.std_luminance_qt, (byte) this.SCALEFACTOR, bArr);
        for (byte b = 0; b <= 63; b = (byte) (b + 1)) {
            jArr[b] = (long) getShort(bArr2[JTables.zigzag[b]]);
        }
        byte b2 = 0;
        for (byte b3 = 0; b3 <= 7; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 <= 7; b4 = (byte) (b4 + 1)) {
                jArr[b2] = (long) (((int) (((float) jArr[b2]) * fArr[b3] * fArr[b4])) * CfgBandwidth.BANDWIDTH_512KBPS);
                b2 = (byte) (b2 + 1);
            }
        }
        this.byte_pos += 64;
    }

    /* access modifiers changed from: package-private */
    public void load_quant_tableCb(long[] jArr) {
        float[] fArr = {1.0f, 1.3870399f, 1.306563f, 1.1758755f, 1.0f, 0.78569496f, 0.5411961f, 0.27589938f};
        byte[] bArr = new byte[64];
        if (this.Mapping == 1) {
            switch (this.selector) {
                case 0:
                    JTables.std_chrominance_qt = JTables.Tbl_000Y;
                    break;
                case 1:
                    JTables.std_chrominance_qt = JTables.Tbl_014Y;
                    break;
                case 2:
                    JTables.std_chrominance_qt = JTables.Tbl_029Y;
                    break;
                case 3:
                    JTables.std_chrominance_qt = JTables.Tbl_043Y;
                    break;
                case 4:
                    JTables.std_chrominance_qt = JTables.Tbl_057Y;
                    break;
                case 5:
                    JTables.std_chrominance_qt = JTables.Tbl_071Y;
                    break;
                case 6:
                    JTables.std_chrominance_qt = JTables.Tbl_086Y;
                    break;
                case IVTPPktHdr.HDR_SIZE:
                    JTables.std_chrominance_qt = JTables.Tbl_100Y;
                    break;
            }
        } else {
            switch (this.selector) {
                case 0:
                    JTables.std_chrominance_qt = JTables.Tbl_000UV;
                    break;
                case 1:
                    JTables.std_chrominance_qt = JTables.Tbl_014UV;
                    break;
                case 2:
                    JTables.std_chrominance_qt = JTables.Tbl_029UV;
                    break;
                case 3:
                    JTables.std_chrominance_qt = JTables.Tbl_043UV;
                    break;
                case 4:
                    JTables.std_chrominance_qt = JTables.Tbl_057UV;
                    break;
                case 5:
                    JTables.std_chrominance_qt = JTables.Tbl_071UV;
                    break;
                case 6:
                    JTables.std_chrominance_qt = JTables.Tbl_086UV;
                    break;
                case IVTPPktHdr.HDR_SIZE:
                    JTables.std_chrominance_qt = JTables.Tbl_100UV;
                    break;
            }
        }
        byte[] bArr2 = set_quant_table(JTables.std_chrominance_qt, (byte) this.SCALEFACTORUV, bArr);
        for (byte b = 0; b <= 63; b = (byte) (b + 1)) {
            jArr[b] = (long) getShort(bArr2[JTables.zigzag[b]]);
        }
        byte b2 = 0;
        for (byte b3 = 0; b3 <= 7; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 <= 7; b4 = (byte) (b4 + 1)) {
                jArr[b2] = (long) (((int) (((float) jArr[b2]) * fArr[b3] * fArr[b4])) * CfgBandwidth.BANDWIDTH_512KBPS);
                b2 = (byte) (b2 + 1);
            }
        }
        this.byte_pos += 64;
    }

    /* access modifiers changed from: package-private */
    public void load_advance_quant_table(long[] jArr) {
        float[] fArr = {1.0f, 1.3870399f, 1.306563f, 1.1758755f, 1.0f, 0.78569496f, 0.5411961f, 0.27589938f};
        byte[] bArr = new byte[64];
        switch (this.advance_selector) {
            case 0:
                JTables.std_luminance_qt = JTables.Tbl_000Y;
                break;
            case 1:
                JTables.std_luminance_qt = JTables.Tbl_014Y;
                break;
            case 2:
                JTables.std_luminance_qt = JTables.Tbl_029Y;
                break;
            case 3:
                JTables.std_luminance_qt = JTables.Tbl_043Y;
                break;
            case 4:
                JTables.std_luminance_qt = JTables.Tbl_057Y;
                break;
            case 5:
                JTables.std_luminance_qt = JTables.Tbl_071Y;
                break;
            case 6:
                JTables.std_luminance_qt = JTables.Tbl_086Y;
                break;
            case IVTPPktHdr.HDR_SIZE:
                JTables.std_luminance_qt = JTables.Tbl_100Y;
                break;
        }
        byte[] bArr2 = set_quant_table(JTables.std_luminance_qt, (byte) this.ADVANCESCALEFACTOR, bArr);
        for (byte b = 0; b <= 63; b = (byte) (b + 1)) {
            jArr[b] = (long) getShort(bArr2[JTables.zigzag[b]]);
        }
        byte b2 = 0;
        for (byte b3 = 0; b3 <= 7; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 <= 7; b4 = (byte) (b4 + 1)) {
                jArr[b2] = (long) (((int) (((float) jArr[b2]) * fArr[b3] * fArr[b4])) * CfgBandwidth.BANDWIDTH_512KBPS);
                b2 = (byte) (b2 + 1);
            }
        }
        this.byte_pos += 64;
    }

    /* access modifiers changed from: package-private */
    public void load_advance_quant_tableCb(long[] jArr) {
        float[] fArr = {1.0f, 1.3870399f, 1.306563f, 1.1758755f, 1.0f, 0.78569496f, 0.5411961f, 0.27589938f};
        byte[] bArr = new byte[64];
        if (this.Mapping == 1) {
            switch (this.advance_selector) {
                case 0:
                    JTables.std_chrominance_qt = JTables.Tbl_000Y;
                    break;
                case 1:
                    JTables.std_chrominance_qt = JTables.Tbl_014Y;
                    break;
                case 2:
                    JTables.std_chrominance_qt = JTables.Tbl_029Y;
                    break;
                case 3:
                    JTables.std_chrominance_qt = JTables.Tbl_043Y;
                    break;
                case 4:
                    JTables.std_chrominance_qt = JTables.Tbl_057Y;
                    break;
                case 5:
                    JTables.std_chrominance_qt = JTables.Tbl_071Y;
                    break;
                case 6:
                    JTables.std_chrominance_qt = JTables.Tbl_086Y;
                    break;
                case IVTPPktHdr.HDR_SIZE:
                    JTables.std_chrominance_qt = JTables.Tbl_100Y;
                    break;
            }
        } else {
            switch (this.advance_selector) {
                case 0:
                    JTables.std_chrominance_qt = JTables.Tbl_000UV;
                    break;
                case 1:
                    JTables.std_chrominance_qt = JTables.Tbl_014UV;
                    break;
                case 2:
                    JTables.std_chrominance_qt = JTables.Tbl_029UV;
                    break;
                case 3:
                    JTables.std_chrominance_qt = JTables.Tbl_043UV;
                    break;
                case 4:
                    JTables.std_chrominance_qt = JTables.Tbl_057UV;
                    break;
                case 5:
                    JTables.std_chrominance_qt = JTables.Tbl_071UV;
                    break;
                case 6:
                    JTables.std_chrominance_qt = JTables.Tbl_086UV;
                    break;
                case IVTPPktHdr.HDR_SIZE:
                    JTables.std_chrominance_qt = JTables.Tbl_100UV;
                    break;
            }
        }
        set_quant_table(JTables.std_chrominance_qt, (byte) this.ADVANCESCALEFACTORUV, bArr);
        for (byte b = 0; b <= 63; b = (byte) (b + 1)) {
            jArr[b] = (long) getShort(bArr[JTables.zigzag[b]]);
        }
        byte b2 = 0;
        for (byte b3 = 0; b3 <= 7; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 <= 7; b4 = (byte) (b4 + 1)) {
                jArr[b2] = (long) (((int) (((float) jArr[b2]) * fArr[b3] * fArr[b4])) * CfgBandwidth.BANDWIDTH_512KBPS);
                b2 = (byte) (b2 + 1);
            }
        }
        this.byte_pos += 64;
    }

    /* access modifiers changed from: package-private */
    public Huffman_table load_Huffman_table(Huffman_table huffman_table, byte[] bArr, short[] sArr, int[] iArr) {
        for (byte b = 1; b <= 16; b = (byte) (b + 1)) {
            huffman_table.Length[b] = bArr[b];
        }
        int i = 0;
        for (byte b2 = 1; b2 <= 16; b2 = (byte) (b2 + 1)) {
            for (byte b3 = 0; b3 < getShort(huffman_table.Length[b2]); b3 = (byte) (b3 + 1)) {
                huffman_table.V[getInt(WORD_hi_lo(b2, b3))] = sArr[i];
                i++;
            }
        }
        int i2 = 0;
        for (byte b4 = 1; b4 <= 16; b4 = (byte) (b4 + 1)) {
            huffman_table.minor_code[b4] = (short) i2;
            for (byte b5 = 1; b5 <= getShort(huffman_table.Length[b4]); b5 = (byte) (b5 + 1)) {
                i2++;
            }
            huffman_table.major_code[b4] = (short) (i2 - 1);
            i2 *= 2;
            if (getShort(huffman_table.Length[b4]) == 0) {
                huffman_table.minor_code[b4] = -1;
                huffman_table.major_code[b4] = 0;
            }
        }
        huffman_table.Len[0] = 2;
        int i3 = 2;
        for (int i4 = 1; i4 < 65535; i4++) {
            if (i4 < iArr[i3]) {
                huffman_table.Len[i4] = (byte) (iArr[i3 + 1] & VideoSettings.GAIN_MAX);
            } else {
                i3 += 2;
                huffman_table.Len[i4] = (byte) (iArr[i3 + 1] & VideoSettings.GAIN_MAX);
            }
        }
        return huffman_table;
    }

    /* access modifiers changed from: package-private */
    public Huffman_table load_Huffman_table_YDC(Huffman_table huffman_table) {
        for (byte b = 1; b <= 16; b = (byte) (b + 1)) {
            huffman_table.Length[b] = JTables.std_dc_luminance_nrcodes[b];
        }
        byte b2 = 0;
        for (byte b3 = 1; b3 <= 16; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 < getShort(huffman_table.Length[b3]); b4 = (byte) (b4 + 1)) {
                huffman_table.V[getInt(WORD_hi_lo(b3, b4))] = JTables.std_dc_luminance_values[b2];
                b2 = (byte) (b2 + 1);
            }
        }
        short s2 = 0;
        for (byte b5 = 1; b5 <= 16; b5 = (byte) (b5 + 1)) {
            huffman_table.minor_code[b5] = s2;
            for (byte b6 = 1; b6 <= getShort(huffman_table.Length[b5]); b6 = (byte) (b6 + 1)) {
                s2 = (short) (s2 + 1);
            }
            huffman_table.major_code[b5] = (short) (s2 - 1);
            s2 = (short) (s2 * 2);
            if (getShort(huffman_table.Length[b5]) == 0) {
                huffman_table.minor_code[b5] = -1;
                huffman_table.major_code[b5] = 0;
            }
        }
        return huffman_table;
    }

    /* access modifiers changed from: package-private */
    public Huffman_table load_Huffman_table_YAC(Huffman_table huffman_table) {
        for (byte b = 1; b <= 16; b = (byte) (b + 1)) {
            huffman_table.Length[b] = JTables.std_ac_luminance_nrcodes[b];
        }
        byte b2 = 0;
        for (byte b3 = 1; b3 <= 16; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 < getShort(huffman_table.Length[b3]); b4 = (byte) (b4 + 1)) {
                huffman_table.V[getInt(WORD_hi_lo(b3, b4))] = JTables.std_ac_luminance_values[b2];
                b2 = (byte) (b2 + 1);
            }
        }
        short s2 = 0;
        for (byte b5 = 1; b5 <= 16; b5 = (byte) (b5 + 1)) {
            huffman_table.minor_code[b5] = s2;
            for (byte b6 = 1; b6 <= getShort(huffman_table.Length[b5]); b6 = (byte) (b6 + 1)) {
                s2 = (short) (s2 + 1);
            }
            huffman_table.major_code[b5] = (short) (s2 - 1);
            s2 = (short) (s2 * 2);
            if (getShort(huffman_table.Length[b5]) == 0) {
                huffman_table.minor_code[b5] = -1;
                huffman_table.major_code[b5] = 0;
            }
        }
        return huffman_table;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v33, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r2v12, resolved type: short */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public void process_Huffman_data_unit(byte b, byte b2, short[] sArr, short s2) {
        Arrays.fill(this.m_DCT_coeff, 0);
        this.min_code = this.m_HTDC[b].minor_code;
        this.huff_values = this.m_HTDC[b].V;
        byte b3 = this.m_HTDC[b].Len[(this.m_RecvBuffer[0] >> 16) & 65535];
        short lookKbits = lookKbits(b3);
        skipKbits(b3);
        byte b4 = (byte) this.huff_values[WORD_hi_lo(b3, (byte) (lookKbits - this.min_code[b3]))];
        if (b4 == 0) {
            this.m_DCT_coeff[s2 + 0] = sArr[0];
        } else {
            this.m_DCT_coeff[s2] = getKbits(b4) + sArr[0];
            sArr[0] = (short) this.m_DCT_coeff[s2];
        }
        this.min_code = this.m_HTAC[b2].minor_code;
        this.huff_values = this.m_HTAC[b2].V;
        byte b5 = 1;
        do {
            byte b6 = this.m_HTAC[b2].Len[(this.m_RecvBuffer[0] >> 16) & 65535];
            short lookKbits2 = lookKbits(b6);
            skipKbits(b6);
            byte b7 = (byte) this.huff_values[WORD_hi_lo(b6, (byte) (lookKbits2 - this.min_code[b6]))];
            byte b8 = (byte) (b7 & 15);
            byte b9 = (byte) ((b7 >> 4) & 15);
            if (b8 != 0) {
                byte b10 = (byte) (b5 + b9);
                b5 = (byte) (b10 + 1);
                this.m_DCT_coeff[JTables.dezigzag[b10] + s2] = getKbits(b8);
            } else if (b9 == 15) {
                b5 = (byte) (b5 + 16);
            } else {
                return;
            }
        } while (b5 < 64);
    }

    /* access modifiers changed from: package-private */
    public final void precalculate_Cr_Cb_tables() {
        for (short s2 = 0; s2 <= 255; s2 = (short) (s2 + 1)) {
            this.m_Cr_tab[s2] = (short) ((int) ((((double) s2) - 128.0d) * 1.402d));
        }
        for (short s3 = 0; s3 <= 255; s3 = (short) (s3 + 1)) {
            this.m_Cb_tab[s3] = (short) ((int) ((((double) s3) - 128.0d) * 1.772d));
        }
        for (short s4 = 0; s4 <= 255; s4 = (short) (s4 + 1)) {
            for (short s5 = 0; s5 <= 255; s5 = (short) (s5 + 1)) {
                this.m_Cr_Cb_green_tab[(s4 << 8) + s5] = (short) ((int) ((-0.34414d * (((double) s5) - 128.0d)) - (0.71414d * (((double) s4) - 128.0d))));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void calculate_tabs() {
        byte[] bArr = new byte[256];
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            for (byte b2 = 0; b2 < 16; b2 = (byte) (b2 + 1)) {
                bArr[(b * 16) + b2] = (byte) (((b / this.YV) * 8) + (b2 / this.YH));
            }
        }
        for (byte b3 = 0; b3 < 8; b3 = (byte) (b3 + 1)) {
            for (byte b4 = 0; b4 < 8; b4 = (byte) (b4 + 1)) {
                this.tab_1[(b3 * 8) + b4] = bArr[(b3 * 16) + b4];
            }
            for (byte b5 = 8; b5 < 16; b5 = (byte) (b5 + 1)) {
                this.tab_2[(b3 * 8) + (b5 - 8)] = bArr[(b3 * 16) + b5];
            }
        }
        for (byte b6 = 8; b6 < 16; b6 = (byte) (b6 + 1)) {
            for (byte b7 = 0; b7 < 8; b7 = (byte) (b7 + 1)) {
                this.tab_3[((b6 - 8) * 8) + b7] = bArr[(b6 * 16) + b7];
            }
            for (byte b8 = 8; b8 < 16; b8 = (byte) (b8 + 1)) {
                this.tab_4[((b6 - 8) * 8) + (b8 - 8)] = bArr[(b6 * 16) + b8];
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void init_jpg_table() {
        this.m_QT = (long[][]) Array.newInstance(Long.TYPE, 4, 64);
        InitColorTable();
        prepare_range_limit_table();
        this.m_HTDC[0] = load_Huffman_table(this.m_HTDC[0], JTables.std_dc_luminance_nrcodes, JTables.std_dc_luminance_values, JTables.DC_LUMINANCE_HUFFMANCODE);
        this.m_HTAC[0] = load_Huffman_table(this.m_HTAC[0], JTables.std_ac_luminance_nrcodes, JTables.std_ac_luminance_values, JTables.AC_LUMINANCE_HUFFMANCODE);
        this.m_HTDC[1] = load_Huffman_table(this.m_HTDC[1], JTables.std_dc_chrominance_nrcodes, JTables.std_dc_chrominance_values, JTables.DC_CHROMINANCE_HUFFMANCODE);
        this.m_HTAC[1] = load_Huffman_table(this.m_HTAC[1], JTables.std_ac_chrominance_nrcodes, JTables.std_ac_chrominance_values, JTables.AC_CHROMINANCE_HUFFMANCODE);
    }

    /* access modifiers changed from: package-private */
    public int init_JPG_decoding() {
        this.byte_pos = 0;
        load_quant_table(this.m_QT[0]);
        load_quant_tableCb(this.m_QT[1]);
        load_advance_quant_table(this.m_QT[2]);
        load_advance_quant_tableCb(this.m_QT[3]);
        return 1;
    }

    /* access modifiers changed from: package-private */
    public final int FIX_G(double d) {
        return (int) ((65536.0d * d) + 0.5d);
    }

    /* access modifiers changed from: package-private */
    public void InitColorTable() {
        int i = 0;
        int i2 = -128;
        int i3 = 0;
        while (i3 < 256) {
            this.m_CrToR[i3] = ((FIX_G(1.597656d) * i2) + CfgBandwidth.BANDWIDTH_256KBPS) >> 16;
            this.m_CbToB[i3] = ((FIX_G(2.015625d) * i2) + CfgBandwidth.BANDWIDTH_256KBPS) >> 16;
            this.m_CrToG[i3] = (((-FIX_G(0.8125d)) * i2) + CfgBandwidth.BANDWIDTH_256KBPS) >> 16;
            this.m_CbToG[i3] = (((-FIX_G(0.390625d)) * i2) + CfgBandwidth.BANDWIDTH_256KBPS) >> 16;
            i3++;
            i2++;
        }
        int i4 = -16;
        while (i < 256) {
            this.m_Y[i] = ((FIX_G(1.164d) * i4) + CfgBandwidth.BANDWIDTH_256KBPS) >> 16;
            i++;
            i4++;
        }
    }

    /* access modifiers changed from: package-private */
    public void resync() {
        this.byte_pos += 2;
        byte_p(this.byte_pos);
        if (getShort(this.bp) == 255) {
            this.byte_pos++;
        }
        this.w1 = WORD_hi_lo(this.bp, (byte) 0);
        byte_p(this.byte_pos);
        if (getShort(this.bp) == 255) {
            this.byte_pos++;
        }
        this.w1 = (short) (this.w1 + getShort(this.bp));
        byte_p(this.byte_pos);
        if (getShort(this.bp) == 255) {
            this.byte_pos++;
        }
        this.w2 = WORD_hi_lo(this.bp, (byte) 0);
        byte_p(this.byte_pos);
        if (getShort(this.bp) == 255) {
            this.byte_pos++;
        }
        this.w2 = (short) (this.w2 + getShort(this.bp));
        this.wordval = getInt(this.w1);
        this.d_k = 0;
        m_DCY[0] = 0;
        DCCb[0] = 0;
        DCCr[0] = 0;
    }

    /* access modifiers changed from: package-private */
    public final byte byte_p(int i) {
        int i2 = i + 1;
        return this.buf[i];
    }

    /* access modifiers changed from: package-private */
    public final int MULTIPLY(int i, int i2) {
        return (i * i2) >> 8;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r14v4, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r0v23, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r12v14, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r12v15, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r14v5, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r9v11, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r12v16, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r14v6, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r9v17, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r9v18, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r12v17, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r7v13, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r9v19, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r12v18, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r7v19, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r7v20, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r9v20, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r6v14, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r7v21, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r9v21, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r6v20, resolved type: short */
    /* JADX DEBUG: Multi-variable search result rejected for r6v21, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r7v22, resolved type: short[] */
    /* JADX DEBUG: Multi-variable search result rejected for r5v11, resolved type: short */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public void IDCT_transform(int i, byte b) {
        int i2;
        int i3 = 0;
        int i4 = 0;
        int i5 = 8;
        int i6 = i;
        while (i5 > 0) {
            if ((this.m_DCT_coeff[i6 + 8] | this.m_DCT_coeff[i6 + 16] | this.m_DCT_coeff[i6 + 24] | this.m_DCT_coeff[i6 + 32] | this.m_DCT_coeff[i6 + 40] | this.m_DCT_coeff[i6 + 48] | this.m_DCT_coeff[i6 + 56]) == 0) {
                int i7 = ((int) (((long) this.m_DCT_coeff[i6 + 0]) * this.m_QT[b][i3 + 0])) >> 16;
                this.workspace[i4 + 0] = i7;
                this.workspace[i4 + 8] = i7;
                this.workspace[i4 + 16] = i7;
                this.workspace[i4 + 24] = i7;
                this.workspace[i4 + 32] = i7;
                this.workspace[i4 + 40] = i7;
                this.workspace[i4 + 48] = i7;
                this.workspace[i4 + 56] = i7;
                i2 = i6 + 1;
                i3++;
                i4++;
                this.nZeroACTerms++;
            } else {
                int i8 = ((int) (((long) this.m_DCT_coeff[i6 + 0]) * this.m_QT[b][i3 + 0])) >> 16;
                int i9 = ((int) (((long) this.m_DCT_coeff[i6 + 16]) * this.m_QT[b][i3 + 16])) >> 16;
                int i10 = ((int) (((long) this.m_DCT_coeff[i6 + 32]) * this.m_QT[b][i3 + 32])) >> 16;
                int i11 = ((int) (((long) this.m_DCT_coeff[i6 + 48]) * this.m_QT[b][i3 + 48])) >> 16;
                int i12 = i8 + i10;
                int i13 = i8 - i10;
                int i14 = i9 + i11;
                int MULTIPLY = MULTIPLY(i9 - i11, 362) - i14;
                int i15 = i12 + i14;
                int i16 = i12 - i14;
                int i17 = i13 + MULTIPLY;
                int i18 = i13 - MULTIPLY;
                int i19 = ((int) (((long) this.m_DCT_coeff[i6 + 8]) * this.m_QT[b][i3 + 8])) >> 16;
                int i20 = ((int) (((long) this.m_DCT_coeff[i6 + 24]) * this.m_QT[b][i3 + 24])) >> 16;
                int i21 = ((int) (((long) this.m_DCT_coeff[i6 + 40]) * this.m_QT[b][i3 + 40])) >> 16;
                int i22 = ((int) (((long) this.m_DCT_coeff[i6 + 56]) * this.m_QT[b][i3 + 56])) >> 16;
                int i23 = i21 + i20;
                int i24 = i21 - i20;
                int i25 = i19 + i22;
                int i26 = i19 - i22;
                int i27 = i25 + i23;
                int MULTIPLY2 = MULTIPLY(i25 - i23, 362);
                int MULTIPLY3 = MULTIPLY(i24 + i26, 473);
                int MULTIPLY4 = (MULTIPLY(i24, -669) + MULTIPLY3) - i27;
                int i28 = MULTIPLY2 - MULTIPLY4;
                int MULTIPLY5 = (MULTIPLY(i26, 277) - MULTIPLY3) + i28;
                this.workspace[i4 + 0] = i15 + i27;
                this.workspace[i4 + 56] = i15 - i27;
                this.workspace[i4 + 8] = i17 + MULTIPLY4;
                this.workspace[i4 + 48] = i17 - MULTIPLY4;
                this.workspace[i4 + 16] = i18 + i28;
                this.workspace[i4 + 40] = i18 - i28;
                this.workspace[i4 + 32] = i16 + MULTIPLY5;
                this.workspace[i4 + 24] = i16 - MULTIPLY5;
                i2 = i6 + 1;
                i3++;
                i4++;
            }
            i5--;
            i6 = i2;
        }
        int i29 = 0;
        for (int i30 = 0; i30 < 8; i30++) {
            int i31 = (i30 * 8) + i;
            int i32 = this.workspace[i29 + 0] + this.workspace[i29 + 4];
            int i33 = this.workspace[i29 + 0] - this.workspace[i29 + 4];
            int i34 = this.workspace[i29 + 2] + this.workspace[i29 + 6];
            int MULTIPLY6 = MULTIPLY(this.workspace[i29 + 2] - this.workspace[i29 + 6], 362) - i34;
            int i35 = i32 + i34;
            int i36 = i32 - i34;
            int i37 = i33 + MULTIPLY6;
            int i38 = i33 - MULTIPLY6;
            int i39 = this.workspace[i29 + 5] + this.workspace[i29 + 3];
            int i40 = this.workspace[i29 + 5] - this.workspace[i29 + 3];
            int i41 = this.workspace[i29 + 1] + this.workspace[i29 + 7];
            int i42 = this.workspace[i29 + 1] - this.workspace[i29 + 7];
            int i43 = i41 + i39;
            int MULTIPLY7 = MULTIPLY(i41 - i39, 362);
            int MULTIPLY8 = MULTIPLY(i40 + i42, 473);
            int MULTIPLY9 = (MULTIPLY(i40, -669) + MULTIPLY8) - i43;
            int i44 = MULTIPLY7 - MULTIPLY9;
            int MULTIPLY10 = (MULTIPLY(i42, 277) - MULTIPLY8) + i44;
            this.m_byTileYuv[i31 + 0] = this.m_rlimit_table_short[(((i35 + i43) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 7] = this.m_rlimit_table_short[(((i35 - i43) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 1] = this.m_rlimit_table_short[(((i37 + MULTIPLY9) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 6] = this.m_rlimit_table_short[(((i37 - MULTIPLY9) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 2] = this.m_rlimit_table_short[(((i38 + i44) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 5] = this.m_rlimit_table_short[(((i38 - i44) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 4] = this.m_rlimit_table_short[(((i36 + MULTIPLY10) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            this.m_byTileYuv[i31 + 3] = this.m_rlimit_table_short[(((i36 - MULTIPLY10) >> 3) & 1023) + USBMessage.IUSB_FROM_REMOTE + 256];
            i29 += 8;
        }
    }

    /* access modifiers changed from: package-private */
    public final void updatereadbuf(int i) {
        if (m_newbits - i <= 0) {
            this.uprdbuf_readbuf = getLong(this.m_RecvBuffer[IVTPPktHdr.VIRTADD + this._index]);
            this._index++;
            this.m_RecvBuffer[0] = ((int) (getLong(this.m_RecvBuffer[0]) << i)) | ((int) ((getLong(this.m_RecvBuffer[1]) | (this.uprdbuf_readbuf >> m_newbits)) >> (32 - i)));
            this.m_RecvBuffer[1] = (int) (this.uprdbuf_readbuf << (i - m_newbits));
            m_newbits = (m_newbits + 32) - i;
            return;
        }
        this.m_RecvBuffer[0] = ((int) (getLong(this.m_RecvBuffer[0]) << i)) | ((int) (getLong(this.m_RecvBuffer[1]) >> (32 - i)));
        this.m_RecvBuffer[1] = (int) (getLong(this.m_RecvBuffer[1]) << i);
        m_newbits -= i;
    }

    /* access modifiers changed from: package-private */
    public void YUVToRGB(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9 = 0;
        if (this.m_Mode420 == 0) {
            this.py = this.m_byTileYuv;
            int i10 = 64;
            for (int i11 = 0; i11 < 64; i11++) {
                this.pcb[i11] = this.m_byTileYuv[i10];
                this.pcr[i11] = this.m_byTileYuv[i10 + 64];
                i10++;
            }
            int i12 = i * 8;
            int i13 = i2 * 8;
            int i14 = (this.RealWIDTH * i13) + i12;
            int i15 = this.RealWIDTH - i12;
            int i16 = (i15 == 0 || i15 > 8) ? 8 : i15;
            int i17 = i14;
            for (int i18 = 0; i18 < 8; i18++) {
                for (int i19 = 0; i19 < i16; i19++) {
                    int i20 = (i18 << 3) + i19;
                    int i21 = this.py[i20];
                    int i22 = this.pcb[i20];
                    int i23 = this.pcr[i20];
                    int i24 = (i17 + i19) * 3;
                    int i25 = this.m_Y[i21] + this.m_CbToB[i22];
                    int i26 = this.m_CrToG[i23] + this.m_CbToG[i22] + this.m_Y[i21];
                    int i27 = this.m_Y[i21] + this.m_CrToR[i23];
                    int i28 = i25 >= 0 ? i25 + 256 : 0;
                    int i29 = i26 >= 0 ? i26 + 256 : 0;
                    if (i27 >= 0) {
                        i8 = i27 + 256;
                    } else {
                        i8 = 0;
                    }
                    if (i24 < this.RealWIDTH * this.RealHEIGHT * 3) {
                        this.m_alovicaBuf[i24] = this.m_rlimit_table[i28];
                        this.m_alovicaBuf[i24 + 1] = this.m_rlimit_table[i29];
                        this.m_alovicaBuf[i24 + 2] = this.m_rlimit_table[i8];
                        this.m_vidClnt.SetPointInSavedScreen(i12 + i19, i13 + i18, this.m_alovicaBuf[i24], this.m_alovicaBuf[i24 + 1], this.m_alovicaBuf[i24 + 2]);
                    }
                }
                i17 = this.RealWIDTH + i17;
            }
            return;
        }
        for (int i30 = 0; i30 < 4; i30++) {
            int i31 = 0;
            while (i31 < 64) {
                this.py420[i30][i31] = this.m_byTileYuv[i9];
                i31++;
                i9++;
            }
        }
        for (int i32 = 0; i32 < 64; i32++) {
            this.pcb[i32] = this.m_byTileYuv[i9];
            this.pcr[i32] = this.m_byTileYuv[i9 + 64];
            i9++;
        }
        int i33 = i * 16;
        int i34 = i2 * 16;
        int i35 = (this.WIDTH * i34) + i33;
        int i36 = 0;
        int i37 = 0;
        int i38 = 0;
        int i39 = (this.HEIGHT == 608 && i2 == 37) ? 8 : 16;
        int i40 = 0;
        int i41 = i35;
        for (int i42 = 0; i42 < i39; i42++) {
            int i43 = (i42 >> 3) * 2;
            int i44 = (i42 >> 1) << 3;
            int i45 = 0;
            while (i45 < 16) {
                int i46 = i43 + (i45 >> 3);
                switch (i46) {
                    case 0:
                        i3 = i40;
                        i4 = i38;
                        i5 = i37;
                        i6 = i36 + 1;
                        i7 = i36;
                        break;
                    case 1:
                        i3 = i40;
                        i4 = i38;
                        i5 = i37 + 1;
                        i6 = i36;
                        i7 = i37;
                        break;
                    case 2:
                        i3 = i40;
                        i4 = i38 + 1;
                        i5 = i37;
                        i6 = i36;
                        i7 = i38;
                        break;
                    default:
                        i3 = i40 + 1;
                        i4 = i38;
                        i5 = i37;
                        i6 = i36;
                        i7 = i40;
                        break;
                }
                int i47 = this.py420[i46][i7];
                int i48 = (i45 >> 1) + i44;
                int i49 = this.pcb[i48];
                int i50 = this.pcr[i48];
                int i51 = this.m_Y[i47] + this.m_CbToB[i49];
                int i52 = this.m_CbToG[i49] + this.m_CrToG[i50] + this.m_Y[i47];
                int i53 = this.m_Y[i47] + this.m_CrToR[i50];
                int i54 = (i41 + i45) * 3;
                if (i51 >= 0) {
                    this.m_alovicaBuf[i54] = this.m_rlimit_table[i51 + 256];
                } else {
                    this.m_alovicaBuf[i54] = 0;
                }
                if (i52 >= 0) {
                    this.m_alovicaBuf[i54 + 1] = this.m_rlimit_table[i52 + 256];
                } else {
                    this.m_alovicaBuf[i54 + 1] = 0;
                }
                if (i53 >= 0) {
                    this.m_alovicaBuf[i54 + 2] = this.m_rlimit_table[i53 + 256];
                } else {
                    this.m_alovicaBuf[i54 + 2] = 0;
                }
                this.m_vidClnt.SetPointInSavedScreen(i33 + i45, i34 + i42, this.m_alovicaBuf[i54], this.m_alovicaBuf[i54 + 1], this.m_alovicaBuf[i54 + 2]);
                i45++;
                i40 = i3;
                i38 = i4;
                i37 = i5;
                i36 = i6;
            }
            i41 = this.RealWIDTH + i41;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean Decompress(int i, int i2, byte b) {
        process_Huffman_data_unit(this.m_YDC_nr, this.m_YAC_nr, m_DCY, 0);
        IDCT_transform(0, b);
        if (this.m_Mode420 == 1) {
            process_Huffman_data_unit(this.m_YDC_nr, this.m_YAC_nr, m_DCY, 64);
            IDCT_transform(64, b);
            process_Huffman_data_unit(this.m_YDC_nr, this.m_YAC_nr, m_DCY, 128);
            IDCT_transform(USBMessage.IUSB_FROM_REMOTE, b);
            if (IVTPPktHdr.gTxb == 0 && IVTPPktHdr.gTyb == 1) {
                IVTPPktHdr.gCheck = 100;
            }
            process_Huffman_data_unit(this.m_YDC_nr, this.m_YAC_nr, m_DCY, 192);
            IDCT_transform(192, b);
            if (IVTPPktHdr.gTxb == 0 && IVTPPktHdr.gTyb == 1) {
                IVTPPktHdr.gCheck = 101;
            }
            process_Huffman_data_unit(this.m_CbDC_nr, this.m_CbAC_nr, DCCb, 256);
            IDCT_transform(256, (byte) (b + 1));
            process_Huffman_data_unit(this.m_CrDC_nr, this.m_CrAC_nr, DCCr, 320);
            IDCT_transform(320, (byte) (b + 1));
        } else {
            process_Huffman_data_unit(this.m_CbDC_nr, this.m_CbAC_nr, DCCb, 64);
            IDCT_transform(64, (byte) (b + 1));
            process_Huffman_data_unit(this.m_CrDC_nr, this.m_CrAC_nr, DCCr, 128);
            IDCT_transform(USBMessage.IUSB_FROM_REMOTE, (byte) (b + 1));
        }
        YUVToRGB(i, i2);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void VQ_Decompress(int i, int i2, byte b) {
        int i3 = 0;
        if (this.m_VQ.BitMapBits == 0) {
            for (int i4 = 0; i4 < 64; i4++) {
                this.m_byTileYuv[i3 + 0] = (char) ((int) ((this.m_VQ.Color[this.m_VQ.Index[0]] & 16711680) >> 16));
                this.m_byTileYuv[i3 + 64] = (char) ((int) ((this.m_VQ.Color[this.m_VQ.Index[0]] & 65280) >> 8));
                this.m_byTileYuv[i3 + USBMessage.IUSB_FROM_REMOTE] = (char) ((int) (this.m_VQ.Color[this.m_VQ.Index[0]] & 255));
                i3++;
            }
        } else {
            for (int i5 = 0; i5 < 64; i5++) {
                short lookKbits = lookKbits(this.m_VQ.BitMapBits);
                this.m_byTileYuv[i3 + 0] = (char) ((int) ((this.m_VQ.Color[this.m_VQ.Index[lookKbits]] & 16711680) >> 16));
                this.m_byTileYuv[i3 + 64] = (char) ((int) ((this.m_VQ.Color[this.m_VQ.Index[lookKbits]] & 65280) >> 8));
                this.m_byTileYuv[i3 + USBMessage.IUSB_FROM_REMOTE] = (char) ((int) (this.m_VQ.Color[this.m_VQ.Index[lookKbits]] & 255));
                i3++;
                skipKbits(this.m_VQ.BitMapBits);
            }
        }
        YUVToRGB(i, i2);
    }

    /* access modifiers changed from: package-private */
    public final void MoveBlockIndex() {
        this.txb++;
        if (this.txb == this.tmp_WIDTHBy16) {
            this.m_view.repaint((this.txb - 1) * 16, this.tyb * 16, 16, 16);
        }
        if (this.m_Mode420 == 0) {
            if (this.txb >= this.tmp_WIDTHBy16 / 8) {
                this.tyb++;
                if (this.tyb >= this.tmp_HEIGHTBy16 / 8) {
                    this.tyb = 0;
                }
                this.txb = 0;
            }
        } else if (this.txb >= this.tmp_WIDTHBy16 / 16) {
            this.tyb++;
            if (this.tyb >= this.tmp_HEIGHTBy16 / 16) {
                this.tyb = 0;
            }
            this.txb = 0;
        }
        IVTPPktHdr.gTxb = this.txb;
        IVTPPktHdr.gTyb = this.tyb;
        this.pixels += 256;
    }

    /* access modifiers changed from: package-private */
    public void decode(VideoEngineInfo videoEngineInfo, int[] iArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            try {
                this.m_VQ.Index[i2] = (char) i2;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.m_VQ.Color[0] = 32896;
        this.m_VQ.Color[1] = 16744576;
        this.m_VQ.Color[2] = 8421504;
        this.m_VQ.Color[3] = 12615808;
        this.WIDTH = videoEngineInfo.SourceModeInfo.X;
        this.HEIGHT = videoEngineInfo.SourceModeInfo.Y;
        this.RealWIDTH = videoEngineInfo.SourceModeInfo.X;
        this.RealHEIGHT = videoEngineInfo.SourceModeInfo.Y;
        this.m_Mode420 = videoEngineInfo.FrameHeader.Mode420;
        if (this.m_Mode420 == 1) {
            if (this.WIDTH % 16 != 0) {
                this.WIDTH = (this.WIDTH + 16) - (this.WIDTH % 16);
            }
            if (this.HEIGHT % 16 != 0) {
                this.HEIGHT = (this.HEIGHT + 16) - (this.HEIGHT % 16);
            }
        } else {
            if (this.WIDTH % 8 != 0) {
                this.WIDTH = (this.WIDTH + 8) - (this.WIDTH % 8);
            }
            if (this.HEIGHT % 8 != 0) {
                this.HEIGHT = (this.HEIGHT + 8) - (this.HEIGHT % 8);
            }
        }
        this.tmp_WIDTHBy16 = videoEngineInfo.DestinationModeInfo.X;
        this.tmp_HEIGHTBy16 = videoEngineInfo.DestinationModeInfo.Y;
        if (this.m_Mode420 == 1) {
            if (this.tmp_WIDTHBy16 % 16 != 0) {
                this.tmp_WIDTHBy16 = (this.tmp_WIDTHBy16 + 16) - (this.tmp_WIDTHBy16 % 16);
            }
            if (this.tmp_HEIGHTBy16 % 16 != 0) {
                this.tmp_HEIGHTBy16 = (this.tmp_HEIGHTBy16 + 16) - (this.tmp_HEIGHTBy16 % 16);
            }
        } else {
            if (this.tmp_WIDTHBy16 % 8 != 0) {
                this.tmp_WIDTHBy16 = (this.tmp_WIDTHBy16 + 8) - (this.tmp_WIDTHBy16 % 8);
            }
            if (this.tmp_HEIGHTBy16 % 8 != 0) {
                this.tmp_HEIGHTBy16 = (this.tmp_HEIGHTBy16 + 8) - (this.tmp_HEIGHTBy16 % 8);
            }
        }
        int i3 = videoEngineInfo.CompressData.CompressSize / 4;
        if (videoEngineInfo.FrameHeader.RC4Enable == 1) {
            if (!this.DecodeRC4State) {
                Keys_Expansion(this.DecodeKeys);
                DecodeRC4_setup(this.DecodeKeys, videoEngineInfo);
            }
            RC4_crypt(this.m_RecvBuffer, (int) (getLong(i3) * 4));
        }
        this.yheader.qfactor = videoEngineInfo.FrameHeader.JPEGScaleFactor;
        this.yheader.height = videoEngineInfo.DestinationModeInfo.Y;
        this.yheader.width = videoEngineInfo.DestinationModeInfo.X;
        int i4 = videoEngineInfo.FrameHeader.NumberOfMB;
        this.SCALEFACTOR = this.yheader.qfactor;
        this.SCALEFACTORUV = this.yheader.qfactor;
        this.ADVANCESCALEFACTOR = videoEngineInfo.FrameHeader.AdvanceScaleFactor;
        this.ADVANCESCALEFACTORUV = videoEngineInfo.FrameHeader.AdvanceScaleFactor;
        this.selector = videoEngineInfo.FrameHeader.JPEGTableSelector;
        this.advance_selector = videoEngineInfo.FrameHeader.AdvanceTableSelector;
        this.Mapping = videoEngineInfo.FrameHeader.JPEGYUVTableMapping;
        this.SharpModeSelection = videoEngineInfo.FrameHeader.SharpModeSelection;
        init_JPG_decoding();
        this.m_RecvBuffer = iArr;
        this._index = 2;
        this.tyb = 0;
        this.txb = 0;
        m_newbits = 32;
        short[] sArr = m_DCY;
        short[] sArr2 = DCCb;
        DCCr[0] = 0;
        sArr2[0] = 0;
        sArr[0] = 0;
        do {
            if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 0) {
                updatereadbuf(4);
                Decompress(this.txb, this.tyb, (byte) 0);
                MoveBlockIndex();
            } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) != 9) {
                if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 8) {
                    this.txb = (int) ((getLong(this.m_RecvBuffer[0]) & 267386880) >> 20);
                    this.tyb = (int) ((getLong(this.m_RecvBuffer[0]) & 1044480) >> 12);
                    updatereadbuf(20);
                    Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 5) {
                    updatereadbuf(4);
                    this.m_VQ.BitMapBits = 0;
                    for (int i5 = 0; i5 < 1; i5++) {
                        this.m_VQ.Index[i5] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i5]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 13) {
                    this.txb = (int) ((getLong(this.m_RecvBuffer[0]) & 267386880) >> 20);
                    this.tyb = (int) ((getLong(this.m_RecvBuffer[0]) & 1044480) >> 12);
                    updatereadbuf(20);
                    this.m_VQ.BitMapBits = 0;
                    for (int i6 = 0; i6 < 1; i6++) {
                        this.m_VQ.Index[i6] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i6]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 6) {
                    updatereadbuf(4);
                    this.m_VQ.BitMapBits = 1;
                    for (int i7 = 0; i7 < 2; i7++) {
                        this.m_VQ.Index[i7] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i7]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 14) {
                    this.txb = (int) ((getLong(this.m_RecvBuffer[0]) & 267386880) >> 20);
                    this.tyb = (int) ((getLong(this.m_RecvBuffer[0]) & 1044480) >> 12);
                    updatereadbuf(20);
                    this.m_VQ.BitMapBits = 1;
                    for (int i8 = 0; i8 < 2; i8++) {
                        this.m_VQ.Index[i8] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i8]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 7) {
                    updatereadbuf(4);
                    this.m_VQ.BitMapBits = 2;
                    for (int i9 = 0; i9 < 4; i9++) {
                        this.m_VQ.Index[i9] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i9]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 15) {
                    this.txb = (int) ((getLong(this.m_RecvBuffer[0]) & 267386880) >> 20);
                    this.tyb = (int) ((getLong(this.m_RecvBuffer[0]) & 1044480) >> 12);
                    updatereadbuf(20);
                    this.m_VQ.BitMapBits = 2;
                    for (int i10 = 0; i10 < 4; i10++) {
                        this.m_VQ.Index[i10] = (char) ((int) ((getLong(this.m_RecvBuffer[0]) >> 29) & 3));
                        if (((getLong(this.m_RecvBuffer[0]) >> 31) & 1) == 0) {
                            updatereadbuf(3);
                        } else {
                            this.m_VQ.Color[this.m_VQ.Index[i10]] = (getLong(this.m_RecvBuffer[0]) >> 5) & 16777215;
                            updatereadbuf(27);
                        }
                    }
                    VQ_Decompress(this.txb, this.tyb, (byte) 0);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 4) {
                    updatereadbuf(4);
                    Decompress(this.txb, this.tyb, (byte) 2);
                    MoveBlockIndex();
                } else if (((getLong(this.m_RecvBuffer[0]) >> 28) & 15) == 12) {
                    this.txb = (int) ((getLong(this.m_RecvBuffer[0]) & 267386880) >> 20);
                    this.tyb = (int) ((getLong(this.m_RecvBuffer[0]) & 1044480) >> 12);
                    updatereadbuf(20);
                    Decompress(this.txb, this.tyb, (byte) 2);
                    MoveBlockIndex();
                }
            } else {
                return;
            }
            this.m_view.repaint((this.txb - 1) * 16, this.tyb * 16, 16, 16);
            i++;
        } while (this._index <= i3);
    }

    /* access modifiers changed from: package-private */
    public void RC4_crypt(int[] iArr, int i) {
        int i2 = this.s.x;
        int i3 = this.s.y;
        this.rc4Tmp = this.s.m;
        for (int i4 = 0; i4 < i; i4++) {
            i2 = (byte) (i2 + 1);
            int i5 = this.rc4Tmp[i2];
            i3 = (byte) (i3 + i5);
            int[] iArr2 = this.rc4Tmp;
            int i6 = this.rc4Tmp[i3];
            iArr2[i2] = i6;
            this.rc4Tmp[i3] = i5;
            iArr[IVTPPktHdr.VIRTADD + i4] = this.rc4Tmp[(byte) (i5 + i6)] ^ iArr[IVTPPktHdr.VIRTADD + i4];
        }
        this.s.x = i2;
        this.s.y = i3;
    }

    /* access modifiers changed from: package-private */
    public void DecodeRC4_setup(byte[] bArr, VideoEngineInfo videoEngineInfo) {
        int[] iArr = new int[256];
        this.s.x = 0;
        this.s.y = 0;
        int[] iArr2 = this.s.m;
        for (int i = 0; i < 256; i++) {
            iArr2[i] = i;
        }
        int i2 = 0;
        byte b = 0;
        int i3 = 0;
        while (i3 < 256) {
            int i4 = iArr2[i3];
            byte b2 = (byte) (b + i4 + bArr[i2]);
            iArr2[i3] = iArr2[b2];
            iArr2[b2] = i4;
            i2++;
            i3++;
            b = b2;
        }
    }

    /* access modifiers changed from: package-private */
    public void Keys_Expansion(byte[] bArr) {
        short length = (short) bArr.length;
        for (short s2 = 0; s2 < 256; s2 = (short) (s2 + 1)) {
            bArr[s2] = bArr[s2 % length];
        }
    }
}
