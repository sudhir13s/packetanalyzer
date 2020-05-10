import packetanalyzer.util.Utilities;

/**
 * The ICMP class.
 */

public class ICMP {

    final byte PACKET_SIZE = 8;
    private static final byte TYPE_SIZE = 1;
    private static final byte CODE_SIZE = 1;
    private static final byte HEADER_CHECKSUM_SIZE = 2;

    int PACKET_COUNT = 0;

    public enum TYPE {
        Echo_reply("0"),
        Echo_request("8");

        private final String value;

        TYPE(String value) {
            this.value = value;
        }

        public String fetchValue() {
            return value;
        }

        public static TYPE getValue(String value) {
            for (TYPE val : TYPE.values()) {
                if (val.fetchValue().equals(value)) return val;
            }
            throw new IllegalArgumentException("TYPE not found.");
        }
    }

    public ICMP() {
    }

    public void printICMPData(byte[] icmpBytes) {
        System.out.println("ICMP:  ------ ICMP Header ------     ");
        System.out.println("ICMP:  ");

        String type = getType(icmpBytes);
        System.out.println("ICMP:  Type = " + type + " (" + TYPE.getValue(type) + ")");
        System.out.println("ICMP:  Code = " + getCode(icmpBytes));

        System.out.println("ICMP:  Checksum = " + getHeaderChecksum(icmpBytes));
        System.out.println("ICMP:  ");
    }

    public String getType(byte[] icmpBytes) {
        byte[] type = Utilities.getBytesInRange(icmpBytes, PACKET_COUNT, TYPE_SIZE);
        PACKET_COUNT += TYPE_SIZE;
        return Utilities.bytesToDecimalString(type);
    }

    public String getCode(byte[] icmpBytes) {
        byte[] code = Utilities.getBytesInRange(icmpBytes, PACKET_COUNT, CODE_SIZE);
        PACKET_COUNT += CODE_SIZE;
        return Utilities.bytesToDecimalString(code);
    }

    public String getHeaderChecksum(byte[] icmpBytes) {
        byte[] headerChecksum = Utilities.getBytesInRange(icmpBytes, PACKET_COUNT, HEADER_CHECKSUM_SIZE);
        PACKET_COUNT += HEADER_CHECKSUM_SIZE;
        return Utilities.bytesToHex(headerChecksum);
    }
}
