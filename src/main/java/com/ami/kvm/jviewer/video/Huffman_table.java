package com.ami.kvm.jviewer.video;

import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import java.util.Arrays;

public class Huffman_table {
    public byte[] Len = new byte[CfgBandwidth.BANDWIDTH_512KBPS];
    public byte[] Length = new byte[17];
    public short[] V = new short[CfgBandwidth.BANDWIDTH_512KBPS];
    public short[] major_code = new short[17];
    public short[] minor_code = new short[17];

    public Huffman_table() {
        Arrays.fill(this.Length, (byte) 0);
        Arrays.fill(this.minor_code, (short) 0);
        Arrays.fill(this.major_code, (short) 0);
        Arrays.fill(this.V, (short) 0);
    }
}
