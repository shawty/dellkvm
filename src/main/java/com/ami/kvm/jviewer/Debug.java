package com.ami.kvm.jviewer;

public class Debug {
    public static final int DEBUG = 1;
    public static int MODE = 0;
    public static final int RELEASE = 2;
    private static final char[] kHexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static Debug out = new Debug();

    private Debug() {
        MODE = 2;
    }

    public void print(Object obj) {
        if (MODE == 1) {
            System.out.print(obj);
        }
    }

    public void println(Object obj) {
        if (MODE == 1) {
            System.out.println(obj);
        }
    }

    public void dump(byte[] bArr) {
        if (MODE == 1) {
            dump(bArr, bArr.length);
        }
    }

    public void dump(byte[] bArr, int i) {
        if (MODE == 1) {
            dump(bArr, 0, i);
        }
    }

    public void dump(byte[] bArr, int i, int i2) {
        if (MODE != 1) {
            return;
        }
        if (bArr.length - i < i2) {
            System.out.println("Invalid buffer");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer(i2 * 2);
        for (int i3 = i; i3 < i + i2; i3++) {
            appendHexPair(bArr[i3], stringBuffer);
        }
        for (int i4 = 0; i4 < stringBuffer.length(); i4 += 60) {
            int i5 = i4 + 60;
            if (i5 > stringBuffer.length()) {
                i5 = stringBuffer.length();
            }
            System.out.println(stringBuffer.substring(i4, i5));
        }
    }

    public static void appendHexPair(byte b, StringBuffer stringBuffer) {
        char c = kHexChars[(b & 240) >> 4];
        char c2 = kHexChars[b & 15];
        stringBuffer.append(c);
        stringBuffer.append(c2);
        stringBuffer.append(' ');
    }
}
