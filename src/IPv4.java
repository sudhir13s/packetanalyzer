import packetanalyzer.util.Utilities;

/**
 * The IPv4 class
 */

public class IPv4 {

    byte VERSION_SIZE = 1;
    byte HEADER_LENGTH_SIZE = 4; // 32-bit
    private static final byte SERVICE_TYPE_SIZE = 1;

    private static final byte TOTAL_LENGTH_SIZE = 2;
    private static final byte IDENTIFICATION_SIZE = 2;


    private static final byte FLAGS_AND_FRAGMENT_OFFSET_SIZE = 2;

    private static final byte TTL_SIZE = 1;
    private static final byte PROTOCOL_SIZE = 1;
    private static final byte HEADER_CHECKSUM_SIZE = 2;

    private static final byte SOURCE_IP_ADDRESS_SIZE = 4;
    private static final byte DESTINATION_IP_ADDRESS_SIZE = 4;

    private static final byte OPTIONS_SIZE = 0;
    private static final byte PADDING_SIZE = 0;

    byte PROTOCOL = 0;
    byte PACKET_SIZE;
    int PACKET_COUNT = 0;

    public enum PROTOCOLS {
        ICMP("1"),
        TCP("6"),
        UDP("17");

        private String value;

        PROTOCOLS(String value) {
            this.value = value;
        }

        public String fetchValue() {
            return value;
        }

        public static PROTOCOLS getValue(String value) {
            for (PROTOCOLS val : PROTOCOLS.values()) {
                if (val.fetchValue().equals(value)) return val;
            }
            throw new IllegalArgumentException("PROTOCOLS not found.");
        }
    }



    public IPv4(int bytePointer) {
//        PACKET_COUNT = bytePointer;
    }

    public void printIPv4Data(byte[] ipv4Bytes) {
        System.out.println("IP:  ------ IP Header ------     ");
        System.out.println("IP:  ");

        System.out.println("IP:  Version = " + getVersion(ipv4Bytes));
        System.out.println("IP:  Header length = " + getIPHeaderLength(ipv4Bytes) + " bytes");

        byte typeOfService = getTypeOfService(ipv4Bytes);

        System.out.println("IP:  Type of service   = " + Utilities.byteToHex(typeOfService));

        System.out.println("IP:     xxx. ....  = " + Integer.toString(typeOfService >>> 5 & 0xFF) + " precedence");

        boolean flag = Utilities.checkBits(typeOfService, 4);
        String flagString = flag ? "Low delay" : "Normal delay";
        System.out.println("IP:     ..." + (flag ? 1 : 0) + " ....  = " + flagString);

        flag = Utilities.checkBits(typeOfService, 3);
        flagString = flag ? "High throughput" : "Normal throughput";
        System.out.println("IP:     .... " + (flag ? 1 : 0) + "...  = " + flagString);

        flag = Utilities.checkBits(typeOfService, 2);
        flagString = flag ? "High reliability" : "Normal reliability";
        System.out.println("IP:     .... ." + (flag ? 1 : 0) + "..  = " + flagString);


        System.out.println("IP:  Total length   = " + getTotalLength(ipv4Bytes) + " bytes");
        System.out.println("IP:  Identification = " + getIdentification(ipv4Bytes));

        byte[] flagAndFragmentOffset = getFlagAndFragmentOffset(ipv4Bytes);

        System.out.println("IP:  Flags  = " + Utilities.byteToHex(flagAndFragmentOffset[0]));

        flag = Utilities.checkBits(flagAndFragmentOffset[0], 6);
        flagString = flag ? "Do not fragment" : "OK to fragment";
        System.out.println("IP:     ." + (flag ? 1 : 0) + ".. ....  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset[0], 5);
        flagString = flag ? "More fragments follow this fragment" : "last fragment";
        System.out.println("IP:     .." + (flag ? 1 : 0) + ". ....  = " + flagString);


        System.out.println("IP:  Fragment offset    = " + "0 bytes");
        System.out.println("IP:  Time to live   = " + getTimeToLive(ipv4Bytes) + " seconds/hops");

        String protocol = getProtocol(ipv4Bytes);
        System.out.println("IP:  Protocol   = " + protocol + " (" + PROTOCOLS.getValue(protocol) + ")");
        System.out.println("IP:  Header checksum    = " + getHeaderChecksum(ipv4Bytes));
        System.out.println("IP:  Source address = " + getSourceAddress(ipv4Bytes));
        System.out.println("IP:  Destination address    = " + getDestinationAddress(ipv4Bytes));
        System.out.println("IP:  No options");
        System.out.println("IP:  ");
    }


    public String getVersion(byte[] ipv4Bytes) {
        byte[] ip_ver_size = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, VERSION_SIZE);
        return Integer.toString(ip_ver_size[0] >>> 4 & 0xFF);
    }

    public String getIPHeaderLength(byte[] ipv4Bytes) {
        byte[] ip_ver_size = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, VERSION_SIZE);
        int header_len = (ip_ver_size[0] & 0xF);
        if (header_len == 0) {
            header_len = 5;
        }
        PACKET_COUNT += 1; // just increase 1 byte
        return Integer.toString(header_len * 4);
    }

    public byte getTypeOfService(byte[] ipv4Bytes) {
        byte[] typeOfService = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, SERVICE_TYPE_SIZE);
        PACKET_COUNT += SERVICE_TYPE_SIZE; // just increase 1 byte
        return typeOfService[0];
    }

    public int getTotalLength(byte[] ipv4Bytes) {
        byte[] totalLength = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, TOTAL_LENGTH_SIZE);
        PACKET_COUNT += TOTAL_LENGTH_SIZE; // just increase 2 byte
        return Utilities.bytesToInt(totalLength);
    }

    public int getIdentification(byte[] ipv4Bytes) {
        byte[] identification = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, IDENTIFICATION_SIZE);
        PACKET_COUNT += IDENTIFICATION_SIZE; // just increase 2 byte
        return Utilities.bytesToInt(identification);
    }

    public byte[] getFlagAndFragmentOffset(byte[] ipv4Bytes) {
        byte[] flagAndFragmentOffset = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, IDENTIFICATION_SIZE);
        PACKET_COUNT += FLAGS_AND_FRAGMENT_OFFSET_SIZE; // just increase 2 byte
        return flagAndFragmentOffset;
    }

    public String getTimeToLive(byte[] ipv4Bytes) {
        byte[] timeToLive = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, TTL_SIZE);
        PACKET_COUNT += TTL_SIZE; // just increase 1 byte
        return Utilities.bytesToDecimalString(timeToLive);
    }

    public String getProtocol(byte[] ipv4Bytes) {
        byte[] protocol = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, PROTOCOL_SIZE);
        PROTOCOL = protocol[0];
        PACKET_COUNT += PROTOCOL_SIZE; // just increase 1 byte
        return Utilities.bytesToDecimalString(protocol);
    }

    public String getHeaderChecksum(byte[] ipv4Bytes) {
        byte[] headerChecksum = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, HEADER_CHECKSUM_SIZE);
        PACKET_COUNT += HEADER_CHECKSUM_SIZE; // just increase 2 byte
        return Utilities.bytesToHex(headerChecksum);
    }

    public String getSourceAddress(byte[] ipv4Bytes) {
        byte[] source = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, SOURCE_IP_ADDRESS_SIZE);
        PACKET_COUNT += SOURCE_IP_ADDRESS_SIZE;
        return Utilities.bytesToDecimalString(source, '.');
    }

    public String getDestinationAddress(byte[] ipv4Bytes) {
        byte[] destination = Utilities.getBytesInRange(ipv4Bytes, PACKET_COUNT, DESTINATION_IP_ADDRESS_SIZE);
        PACKET_COUNT += DESTINATION_IP_ADDRESS_SIZE;
        return Utilities.bytesToDecimalString(destination, '.');
    }


}
