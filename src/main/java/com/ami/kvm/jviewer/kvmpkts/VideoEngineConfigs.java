package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.Debug;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VideoEngineConfigs {
    public static final int VIDEOENGINECONFIG_PKT_SIZE = 8;
    public byte compression_mode;
    public byte dct_quant_quality;
    public byte dct_quant_tbl_select;
    public byte differential_setting;
    public byte disable_hw_cursor;
    private ByteBuffer m_buf;
    public byte sharp_mode_selection;
    public byte sharp_quant_quality;
    public byte sharp_quant_tbl_select;

    public VideoEngineConfigs() {
        this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }

    public VideoEngineConfigs(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        this.differential_setting = b;
        this.dct_quant_quality = b2;
        this.dct_quant_tbl_select = b3;
        this.sharp_mode_selection = b4;
        this.sharp_quant_quality = b5;
        this.sharp_quant_tbl_select = b6;
        this.compression_mode = b7;
        this.disable_hw_cursor = b8;
        this.m_buf = ByteBuffer.wrap(new byte[8]);
        this.m_buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void setVideoEngineConfigs(ByteBuffer byteBuffer) {
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.differential_setting = byteBuffer.get();
        this.dct_quant_quality = byteBuffer.get();
        this.dct_quant_tbl_select = byteBuffer.get();
        this.sharp_mode_selection = byteBuffer.get();
        this.sharp_quant_quality = byteBuffer.get();
        this.sharp_quant_tbl_select = byteBuffer.get();
        this.compression_mode = byteBuffer.get();
        this.disable_hw_cursor = byteBuffer.get();
        Debug.out.println("Get Video Engine Configs Values set");
    }

    public final int size() {
        return 8;
    }

    public final byte[] array() {
        this.m_buf.position(0);
        this.m_buf.put(this.differential_setting);
        this.m_buf.put(this.dct_quant_quality);
        this.m_buf.put(this.dct_quant_tbl_select);
        this.m_buf.put(this.sharp_mode_selection);
        this.m_buf.put(this.sharp_quant_quality);
        this.m_buf.put(this.sharp_quant_tbl_select);
        this.m_buf.put(this.compression_mode);
        this.m_buf.put(this.disable_hw_cursor);
        return this.m_buf.array();
    }
}
