package packetanalyzer.util;

import java.util.Arrays;

public class Utilities {

    public static byte[] getBytesInRange(byte[] bytes, int from, int size) {
        byte[] byteInRange = new byte[size];
        for (int i = 0; i < size; i++) {
            byteInRange[i] = bytes[from++];
        }
        return byteInRange;
    }

    public static String byteToHex(byte b) {
        StringBuilder hex = new StringBuilder();
        hex.append("0x");
        hex.append(String.format("%02x", b));
        return hex.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        hex.append("0x");
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.format("%02x", bytes[i]));
        }
        return hex.toString();
    }

    public static String bytesToHexDelimited(byte[] bytes, char delimiter) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.format("%02x%s", bytes[i], (i < bytes.length - 1) ? delimiter : ""));
        }
        return hex.toString();
    }

    public static String bytesToHexData(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        StringBuilder hexDisplay = new StringBuilder();
        for (int i = 0, j = 1; i < bytes.length; i += 2, j += 2) {
            String str1 = "";
            String str2 = "";
            if (j < bytes.length) {
                str1 = String.format("%02x", bytes[i]) + String.format("%02x", bytes[j]);
                hex.append(str1).append(" ");
                if ((bytes[i]&0xff)<31 || ((bytes[i]&0xff)>126))
                    bytes[i]=46;
                if((bytes[j]&0xff)<31 || ((bytes[j]&0xff)>126)){
                    bytes[j]=46;
                }
                str2 = String.format("%02x", bytes[i]) + String.format("%02x", bytes[j]);
                hexDisplay.append(HexToString(str2));

            } else {
                str1 = String.format("%02x", bytes[i]);
                hex.append(str1).append(" ");
                if ((bytes[i]&0xff)<31 || ((bytes[i]&0xff)>126))
                    bytes[i]=46;
                str2 = String.format("%02x", bytes[i]);
                hexDisplay.append(HexToString(str2));
            }
        }
        return hex.toString() + "   " + hexDisplay.toString();
    }

    public static String HexToString(String str) {
        StringBuilder hexStr = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            String temp = str.substring(i, i + 2);
            hexStr.append((char) Integer.parseInt(temp, 16));
        }
        return hexStr.toString();
    }

    public static String bytesToDecimalString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.format("%d", ((bytes[i]) & 0xFF)));
        }
        return hex.toString();
    }

    public static String bytesToDecimalString(byte[] bytes, char delimiter) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.format("%d%s", ((bytes[i]) & 0xFF), (i < bytes.length - 1) ? delimiter : ""));
        }
        return hex.toString();
    }

    public static String bytesToDecimal(byte[] bytes) {
        int[] decimal = new int[bytes.length];
        int i = 0;
        for (byte b : bytes)
            decimal[i++] = ((int) b & 0xff);
        return Arrays.toString(decimal);
    }

    public static int bytesToInt(byte[] b) {
        int value = 0;
        if (b.length == 1)
            value = 0x00 << 24 | 0x00 << 16 | 0x00 << 8 | (b[0] & 0xff);
        else if (b.length == 2)
            value = 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);
        else if (b.length == 3)
            value = 0x00 << 24 | (b[0] & 0xff) << 16 | (b[1] & 0xff) << 8 | (b[2] & 0xff);
        else if (b.length == 4) {
            value = b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
        }
        return value;
    }

    public static long bytesToLong(byte[] b) {
        long value = 0;
        if (b.length == 4) {
//            value = 0x00 << 56 | 0x00 << 48 | 0x00 << 40 | 0x00 << 32 | (b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff) << 0;
            value = ((long)((b[0] & 0xff)) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff) << 0);
        }
//        if (b.length == 8)
//            value = (b[0] & 0xff) << 56 | (b[1] & 0xff) << 48 | (b[2] & 0xff) << 40 | (b[3] & 0xff) << 32 | (b[4] & 0xff) << 24 | (b[5] & 0xff) << 16 | (b[6] & 0xff) << 8 | (b[7] & 0xff) << 0;
//        else if (b.length == 7)
//            value = 0x00 << 56 | (b[0] & 0xff) << 48 | (b[1] & 0xff) << 40 | (b[2] & 0xff) << 32 | (b[3] & 0xff) << 24 | (b[4] & 0xff) << 16 | (b[5] & 0xff) << 8 | (b[6] & 0xff) << 0;
//        else if (b.length == 6)
//            value = 0x00 << 56 | 0x00 << 48 | (b[0] & 0xff) << 40 | (b[1] & 0xff) << 32 | (b[2] & 0xff) << 24 | (b[3] & 0xff) << 16 | (b[4] & 0xff) << 8 | (b[5] & 0xff) << 0;
//        else if (b.length == 5)
//            value = 0x00 << 56 | 0x00 << 48 | 0x00 << 40 | (b[0] & 0xff) << 32 | (b[1] & 0xff) << 24 | (b[2] & 0xff) << 16 | (b[3] & 0xff) << 8 | (b[4] & 0xff) << 0;
//        else if (b.length == 4)
//            value = 0x00 << 56 | 0x00 << 48 | 0x00 << 40 | 0x00 << 32 | (b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff) << 0;
//        else if (b.length == 3)
//            value = 0x00 << 56 | 0x00 << 48 | 0x00 << 40 | 0x00 << 32 | 0x00 << 24 | (b[0] & 0xff) << 16 | (b[1] & 0xff) << 8 | (b[2] & 0xff) << 0;
//        else if (b.length == 2)
//            value = 0x00 << 56 | 0x00 << 48 | 0x00 << 40 | 0x00 << 32 | 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff) << 0;

//        if (value < 0)
//            value = value >>> 1;
        return value;
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static int hexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static boolean checkBits(byte i, int j) {
        return (((i >>> j) & 1) != 0);
    }


}
