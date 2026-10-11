package com.ami.kvm.jviewer.kvmpkts;

import com.ami.kvm.jviewer.gui.JVMenu;
import com.ami.kvm.jviewer.gui.JViewerApp;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class USBKeyProcessor implements KeyProcessor {
    static final int USB_KEYBOARD_REPORT_SIZE = 8;
    private static byte modifiers = 0;
    private boolean autoKeybreakModeOn = false;
    protected HashMap<Integer, Integer> keypadMap = new HashMap<>();
    protected HashMap<Integer, Integer> leftMap = new HashMap<>();
    private int lngindex;
    protected HashMap<Integer, Integer> rightMap = new HashMap<>();
    protected HashMap<Integer, Integer> standardMap = new HashMap<>();

    public USBKeyProcessor() {
        JViewerApp.getInstance();
        this.lngindex = JViewerApp.getLangIndex();
        if (this.lngindex == 16 && !System.getProperty("os.name").startsWith("Windows") && System.getProperty("os.arch").startsWith("x86")) {
            this.lngindex = 17;
        }
        this.standardMap.put(65, 4);
        this.standardMap.put(66, 5);
        this.standardMap.put(67, 6);
        this.standardMap.put(68, 7);
        this.standardMap.put(69, 8);
        this.standardMap.put(70, 9);
        this.standardMap.put(71, 10);
        this.standardMap.put(72, 11);
        this.standardMap.put(73, 12);
        this.standardMap.put(74, 13);
        this.standardMap.put(75, 14);
        this.standardMap.put(76, 15);
        this.standardMap.put(77, 16);
        this.standardMap.put(78, 17);
        this.standardMap.put(79, 18);
        this.standardMap.put(80, 19);
        this.standardMap.put(81, 20);
        this.standardMap.put(82, 21);
        this.standardMap.put(83, 22);
        this.standardMap.put(84, 23);
        this.standardMap.put(85, 24);
        this.standardMap.put(86, 25);
        this.standardMap.put(87, 26);
        this.standardMap.put(88, 27);
        this.standardMap.put(89, 28);
        this.standardMap.put(90, 29);
        this.standardMap.put(49, 30);
        this.standardMap.put(50, 31);
        this.standardMap.put(51, 32);
        this.standardMap.put(52, 33);
        this.standardMap.put(53, 34);
        this.standardMap.put(54, 35);
        this.standardMap.put(55, 36);
        this.standardMap.put(56, 37);
        this.standardMap.put(57, 38);
        this.standardMap.put(48, 39);
        this.standardMap.put(10, 40);
        this.standardMap.put(27, 41);
        this.standardMap.put(8, 42);
        this.standardMap.put(9, 43);
        this.standardMap.put(32, 44);
        this.standardMap.put(45, 45);
        this.standardMap.put(61, 46);
        if (this.lngindex != 16) {
            this.standardMap.put(91, 47);
        }
        if (this.lngindex != 16) {
            this.standardMap.put(93, 48);
        }
        if (this.lngindex != 16) {
            this.standardMap.put(92, 49);
        }
        this.standardMap.put(59, 51);
        this.standardMap.put(222, 52);
        if (this.lngindex != 16) {
            this.standardMap.put(192, 53);
        }
        this.standardMap.put(44, 54);
        this.standardMap.put(46, 55);
        this.standardMap.put(47, 56);
        this.standardMap.put(20, 57);
        this.standardMap.put(112, 58);
        this.standardMap.put(113, 59);
        this.standardMap.put(114, 60);
        this.standardMap.put(115, 61);
        this.standardMap.put(116, 62);
        this.standardMap.put(117, 63);
        this.standardMap.put(118, 64);
        this.standardMap.put(119, 65);
        this.standardMap.put(120, 66);
        this.standardMap.put(121, 67);
        this.standardMap.put(122, 68);
        this.standardMap.put(123, 69);
        this.standardMap.put(154, 70);
        this.standardMap.put(145, 71);
        this.standardMap.put(19, 72);
        this.standardMap.put(155, 73);
        this.standardMap.put(36, 74);
        this.standardMap.put(33, 75);
        this.standardMap.put(127, 76);
        this.standardMap.put(35, 77);
        this.standardMap.put(34, 78);
        this.standardMap.put(39, 79);
        this.standardMap.put(37, 80);
        this.standardMap.put(40, 81);
        this.standardMap.put(38, 82);
        this.keypadMap.put(144, 83);
        this.keypadMap.put(111, 84);
        this.keypadMap.put(106, 85);
        this.keypadMap.put(109, 86);
        this.keypadMap.put(107, 87);
        this.keypadMap.put(10, 88);
        this.keypadMap.put(97, 89);
        this.keypadMap.put(35, 89);
        this.keypadMap.put(98, 90);
        this.keypadMap.put(40, 90);
        this.keypadMap.put(225, 90);
        this.keypadMap.put(99, 91);
        this.keypadMap.put(34, 91);
        this.keypadMap.put(100, 92);
        this.keypadMap.put(37, 92);
        this.keypadMap.put(226, 92);
        this.keypadMap.put(101, 93);
        this.keypadMap.put(65368, 93);
        this.keypadMap.put(102, 94);
        this.keypadMap.put(39, 94);
        this.keypadMap.put(227, 94);
        this.keypadMap.put(103, 95);
        this.keypadMap.put(36, 95);
        this.keypadMap.put(104, 96);
        this.keypadMap.put(38, 96);
        this.keypadMap.put(224, 96);
        this.keypadMap.put(105, 97);
        this.keypadMap.put(33, 97);
        this.keypadMap.put(96, 98);
        this.keypadMap.put(155, 98);
        this.keypadMap.put(110, 99);
        this.keypadMap.put(127, 99);
        this.keypadMap.put(61, 103);
        this.standardMap.put(61440, 104);
        this.standardMap.put(61441, 105);
        this.standardMap.put(61442, 106);
        this.standardMap.put(61443, 107);
        this.standardMap.put(61444, 108);
        this.standardMap.put(61445, 109);
        this.standardMap.put(61446, 110);
        this.standardMap.put(61447, 111);
        this.standardMap.put(61448, 112);
        this.standardMap.put(61449, 113);
        this.standardMap.put(61450, 114);
        this.standardMap.put(61451, 115);
        this.standardMap.put(156, 117);
        this.standardMap.put(65480, 120);
        this.standardMap.put(65481, 121);
        this.standardMap.put(65483, 122);
        this.standardMap.put(65489, 123);
        this.standardMap.put(65485, 124);
        this.standardMap.put(65487, 125);
        this.standardMap.put(65488, 126);
        this.standardMap.put(3, 155);
        this.standardMap.put(12, 156);
        this.standardMap.put(108, 159);
        if (this.lngindex == 16) {
            this.standardMap.put(514, 46);
            this.standardMap.put(512, 47);
            this.standardMap.put(91, 48);
            this.standardMap.put(93, 49);
            this.standardMap.put(513, 52);
            this.standardMap.put(92, 137);
            this.standardMap.put(523, 135);
            this.standardMap.put(244, 53);
            this.standardMap.put(243, 53);
            this.standardMap.put(28, 138);
            this.standardMap.put(29, 139);
            this.standardMap.put(240, 136);
        }
        if (this.lngindex == 18) {
            this.standardMap.put(520, 520);
        }
        this.leftMap.put(17, 224);
        this.leftMap.put(16, 225);
        this.leftMap.put(18, 226);
        this.leftMap.put(524, 227);
        this.rightMap.put(17, 228);
        this.rightMap.put(16, 229);
        this.rightMap.put(18, 230);
        this.rightMap.put(524, 231);
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.KeyProcessor
    public void setAutoKeybreakMode(boolean z) {
        this.autoKeybreakModeOn = z;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.KeyProcessor
    public boolean getAutoKeybreakMode() {
        return this.autoKeybreakModeOn;
    }

    @Override // com.ami.kvm.jviewer.kvmpkts.KeyProcessor
    public byte[] convertKeyCode(int i, int i2, boolean z) {
        boolean z2;
        Integer num = new Integer(0);
        byte[] bArr = new byte[6];
        switch (i2) {
            case 0:
                JViewerApp.getInstance();
                if (JViewerApp.getLangIndex() == 16 && !System.getProperty("os.name").startsWith("Windows") && System.getProperty("os.arch").startsWith("x86")) {
                    if (i != 47) {
                        if (i != 92) {
                            if (i != 523) {
                                System.err.println("Key located in unknown position");
                                break;
                            } else {
                                num = 135;
                                break;
                            }
                        } else {
                            num = 137;
                            break;
                        }
                    } else {
                        num = 56;
                        break;
                    }
                } else {
                    System.err.println("Key located in unknown position");
                    break;
                }
                break;
            case 1:
                num = this.standardMap.get(Integer.valueOf(i));
                break;
            case 2:
                num = this.leftMap.get(Integer.valueOf(i));
                break;
            case 3:
                num = this.rightMap.get(Integer.valueOf(i));
                break;
            case 4:
                num = this.keypadMap.get(Integer.valueOf(i));
                break;
            default:
                JViewerApp.getInstance();
                if (JViewerApp.getLangIndex() != 16 || System.getProperty("os.name").startsWith("Windows") || !System.getProperty("os.arch").startsWith("x86") || i2 != 16 || i != 92) {
                    System.err.println("Unknown key position value");
                    break;
                } else {
                    num = 137;
                    break;
                }
                break;
        }
        if (num != null) {
            switch (i) {
                case 16:
                    if (i2 != 2) {
                        if (!z) {
                            modifiers = (byte) (modifiers & -33);
                            z2 = false;
                            break;
                        } else {
                            modifiers = (byte) (modifiers | 32);
                            z2 = false;
                            break;
                        }
                    } else if (!z) {
                        modifiers = (byte) (modifiers & -3);
                        z2 = false;
                        break;
                    } else {
                        modifiers = (byte) (modifiers | 2);
                        z2 = false;
                        break;
                    }
                case JVMenu.LANGUAGE_JAPANESE_LINUX:
                    if (i2 != 2) {
                        if (!z) {
                            modifiers = (byte) (modifiers & -17);
                            z2 = false;
                            break;
                        } else {
                            modifiers = (byte) (modifiers | 16);
                            z2 = false;
                            break;
                        }
                    } else if (!z) {
                        modifiers = (byte) (modifiers & -2);
                        z2 = false;
                        break;
                    } else {
                        modifiers = (byte) (modifiers | 1);
                        z2 = false;
                        break;
                    }
                case 18:
                    if (i2 != 2) {
                        if (!z) {
                            modifiers = (byte) (modifiers & -65);
                            z2 = false;
                            break;
                        } else {
                            modifiers = (byte) (modifiers | 64);
                            z2 = false;
                            break;
                        }
                    } else if (!z) {
                        modifiers = (byte) (modifiers & -5);
                        z2 = false;
                        break;
                    } else {
                        modifiers = (byte) (modifiers | 4);
                        z2 = false;
                        break;
                    }
                case 524:
                    if (i2 != 2) {
                        if (!z) {
                            modifiers = (byte) (modifiers & -129);
                            z2 = false;
                            break;
                        } else {
                            modifiers = (byte) (modifiers | 128);
                            z2 = false;
                            break;
                        }
                    } else if (!z) {
                        modifiers = (byte) (modifiers & -9);
                        z2 = false;
                        break;
                    } else {
                        modifiers = (byte) (modifiers | 8);
                        z2 = false;
                        break;
                    }
                default:
                    if (z) {
                        bArr[0] = num.byteValue();
                        if (!this.autoKeybreakModeOn) {
                            z2 = false;
                            break;
                        } else {
                            z2 = true;
                            break;
                        }
                    } else if (!this.autoKeybreakModeOn || i == 144 || i == 20 || i == 145) {
                        bArr[0] = 0;
                        z2 = false;
                        break;
                    } else {
                        return null;
                    }
                    break;
            }
            return USBKeyboardRepPkt(bArr, modifiers, z2);
        }
        System.err.println("Unknown keycode for key: " + KeyEvent.getKeyText(i) + "<" + i + ">");
        return null;
    }

    public byte[] USBKeyboardRepPkt(byte[] bArr, byte b, boolean z) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.put(b);
        allocate.put(z ? (byte) 1 : 0);
        allocate.put(bArr);
        return allocate.array();
    }
}
