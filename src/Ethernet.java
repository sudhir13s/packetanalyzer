import packetanalyzer.util.Utilities;

/**
 * The ethernet class.
 */
public class Ethernet {

    final byte ETHERNET_SIZE = 14;
    private static final byte DESTINATION_START = 0;
    private static final byte DESTINATION_SIZE = 6;

    private static final byte SOURCE_START = 6;
    private static final byte SOURCE_SIZE = 6;

    private static final byte ETHER_TYPE_START = 12;
    private static final byte ETHER_TYPE_SIZE = 2;

    private static int PACKET_SIZE;

    public enum ETHERTYPE {
        IPv4("0x0800"),
        IPv6("0x86DD");

        private final String value;

        ETHERTYPE(String value) {
            this.value = value;
        }

        public String fetchValue() {
            return value;
        }

        public static ETHERTYPE getValue(String value) {
            for (ETHERTYPE val : ETHERTYPE.values()) {
                if (val.fetchValue().equals(value)) return val;
            }
            throw new IllegalArgumentException("ETHERTYPE not found.");
        }
    }

    public Ethernet(byte[] fileContents) {
        PACKET_SIZE = fileContents.length;
    }

    public void printEthernetData(byte[] ethernetBytes) {
        System.out.println("ETHER:  ------ Ether Header ------     ");
        System.out.println("ETHER:  ");
        System.out.println("ETHER:  Packet size = " + getPacketSize());
        System.out.println("ETHER:  Destination = " + getDestinationMAC(ethernetBytes) + ",");
        System.out.println("ETHER:  Source      = " + getSourceMAC(ethernetBytes) + ",");
        System.out.println("ETHER:  EtherType   = " + getEtherTypeFormatted(ethernetBytes));
        System.out.println("ETHER:  ");
    }

    public String getPacketSize() {
        return PACKET_SIZE + " bytes";
    }

    public String getDestinationMAC(byte[] ethernetBytes) {
        byte[] destination = Utilities.getBytesInRange(ethernetBytes, DESTINATION_START, DESTINATION_SIZE);
        return Utilities.bytesToHexDelimited(destination, ':');
    }

    public String getSourceMAC(byte[] ethernetBytes) {
        byte[] source = Utilities.getBytesInRange(ethernetBytes, SOURCE_START, SOURCE_SIZE);
        return Utilities.bytesToHexDelimited(source, ':');
    }

    public byte[] getEtherType(byte[] ethernetBytes) {
        return Utilities.getBytesInRange(ethernetBytes, ETHER_TYPE_START, ETHER_TYPE_SIZE);
    }

    public String getEtherTypeFormatted(byte[] ethernetBytes) {
        byte[] etherType = getEtherType(ethernetBytes);
        String etherTypeHex = Utilities.bytesToHex(etherType);
        return etherTypeHex + " (" + ETHERTYPE.getValue(etherTypeHex) + ")";
    }
}
