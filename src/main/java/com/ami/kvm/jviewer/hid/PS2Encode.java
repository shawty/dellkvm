package com.ami.kvm.jviewer.hid;

import java.awt.event.KeyEvent;

public class PS2Encode extends KMEncode {
    public static final byte EXT_KEY_BREAK = 3;
    public static final byte EXT_KEY_MAKE = 1;
    public static final byte ORD_KEY_BREAK = 2;
    public static final byte ORD_KEY_MAKE = 0;
    private static int[][] m_ps2ExtKeys = {new int[]{154, 124}, new int[]{155, 112}, new int[]{36, 108}, new int[]{33, 125}, new int[]{127, 113}, new int[]{35, 105}, new int[]{34, 122}, new int[]{39, 116}, new int[]{37, 107}, new int[]{40, 114}, new int[]{38, 117}, new int[]{111, 74}};
    private static int[][] m_ps2Keys = {new int[]{65, 28}, new int[]{66, 50}, new int[]{67, 33}, new int[]{68, 35}, new int[]{69, 36}, new int[]{70, 43}, new int[]{71, 52}, new int[]{72, 51}, new int[]{73, 67}, new int[]{74, 59}, new int[]{75, 66}, new int[]{76, 75}, new int[]{77, 58}, new int[]{78, 49}, new int[]{79, 68}, new int[]{80, 77}, new int[]{81, 21}, new int[]{82, 45}, new int[]{83, 27}, new int[]{84, 44}, new int[]{85, 60}, new int[]{86, 42}, new int[]{87, 29}, new int[]{88, 34}, new int[]{89, 53}, new int[]{90, 26}, new int[]{49, 22}, new int[]{50, 30}, new int[]{51, 38}, new int[]{52, 37}, new int[]{53, 46}, new int[]{54, 54}, new int[]{55, 61}, new int[]{56, 62}, new int[]{57, 70}, new int[]{48, 69}, new int[]{10, 90}, new int[]{27, 118}, new int[]{8, 102}, new int[]{9, 13}, new int[]{32, 41}, new int[]{45, 78}, new int[]{61, 85}, new int[]{91, 84}, new int[]{93, 91}, new int[]{92, 93}, new int[]{59, 76}, new int[]{222, 82}, new int[]{192, 14}, new int[]{44, 65}, new int[]{46, 73}, new int[]{47, 74}, new int[]{20, 88}, new int[]{112, 5}, new int[]{113, 6}, new int[]{114, 4}, new int[]{115, 12}, new int[]{116, 3}, new int[]{117, 11}, new int[]{118, 131}, new int[]{119, 10}, new int[]{120, 1}, new int[]{121, 9}, new int[]{122, 120}, new int[]{123, 7}, new int[]{145, 126}, new int[]{144, 119}, new int[]{17, 20}, new int[]{16, 18}, new int[]{18, 17}, new int[]{97, 22}, new int[]{98, 30}, new int[]{99, 38}, new int[]{100, 37}, new int[]{101, 46}, new int[]{102, 54}, new int[]{103, 61}, new int[]{104, 62}, new int[]{105, 70}, new int[]{96, 69}, new int[]{106, 124}, new int[]{107, 121}, new int[]{109, 123}, new int[]{110, 113}, new int[]{96, 112}, new int[]{97, 105}, new int[]{98, 114}, new int[]{99, 122}, new int[]{100, 107}, new int[]{101, 115}, new int[]{102, 116}, new int[]{103, 108}, new int[]{104, 117}, new int[]{105, 125}};

    @Override // com.ami.kvm.jviewer.hid.KMEncode
    public int encodeKey(KeyEvent keyEvent, byte[] bArr) {
        byte b;
        int i;
        int keyCode = keyEvent.getKeyCode();
        int i2 = 0;
        while (true) {
            if (i2 >= m_ps2Keys.length) {
                b = 0;
                break;
            } else if (m_ps2Keys[i2][0] == keyCode) {
                b = (byte) m_ps2Keys[i2][1];
                break;
            } else {
                i2++;
            }
        }
        if (i2 >= m_ps2Keys.length) {
            int i3 = 0;
            while (true) {
                if (i3 >= m_ps2ExtKeys.length) {
                    break;
                } else if (m_ps2ExtKeys[i3][0] == keyCode) {
                    b = (byte) m_ps2ExtKeys[i3][1];
                    break;
                } else {
                    i3++;
                }
            }
        }
        if (i2 < m_ps2Keys.length) {
            if (keyEvent.getID() == 401) {
                bArr[0] = 0;
                i = 1;
            } else {
                bArr[0] = 2;
                bArr[1] = -16;
                i = 2;
            }
        } else if (keyEvent.getID() == 401) {
            bArr[0] = 1;
            bArr[1] = -32;
            i = 2;
        } else {
            bArr[0] = 3;
            bArr[1] = -32;
            i = 3;
            bArr[2] = -16;
        }
        int i4 = i + 1;
        bArr[i] = b;
        return i4;
    }
}
