import packetanalyzer.util.Utilities;

/**
 * The UDP class.
 */

public class UDP {

//    final byte PACKET_SIZE = 8;
    private static final byte SOURCE_PORT_SIZE = 2;
    private static final byte DESTINATION_PORT_SIZE = 2;
    private static final byte LENGTH_SIZE = 2;
    private static final byte HEADER_CHECKSUM_SIZE = 2;

    int FIRST_64_BYTES_DATA = 64;
    int DATA_SIZE = 64;
    int PACKET_COUNT = 0;

    public UDP() {
    }

    public void printUDPData(byte[] udpBytes) {
        System.out.println("UDP:  ------ UDP Header ------     ");
        System.out.println("UDP:  ");

        System.out.println("UDP:  Source port = " + getSourcePort(udpBytes));
        System.out.println("UDP:  Destination port = " + getDestinationPort(udpBytes));

        System.out.println("UDP:  Length = " + getLength(udpBytes));
        System.out.println("UDP:  Checksum = " + getHeaderChecksum(udpBytes));
        System.out.println("UDP:  ");

        System.out.println("UDP:  Data: (first 64 bytes)");
        byte[] data = Utilities.getBytesInRange(udpBytes, 0,  DATA_SIZE);
        int byte_add = 16;

        for (int i = 0; i < FIRST_64_BYTES_DATA && (i <= data.length); i += 16) {
            if (i + byte_add > data.length) {
                byte_add = data.length - i;
            }
                System.out.println("UDP:  " + getData(Utilities.getBytesInRange(data, i, byte_add)));
            }
    }

    public int getSourcePort(byte[] udpBytes) {
        byte[] sourcePort = Utilities.getBytesInRange(udpBytes, PACKET_COUNT, SOURCE_PORT_SIZE);
        PACKET_COUNT += SOURCE_PORT_SIZE;
        return Utilities.bytesToInt(sourcePort);
    }

    public int getDestinationPort(byte[] udpBytes) {
        byte[] destinationPort = Utilities.getBytesInRange(udpBytes, PACKET_COUNT, DESTINATION_PORT_SIZE);
        PACKET_COUNT += DESTINATION_PORT_SIZE;
        return Utilities.bytesToInt(destinationPort);
    }

    public int getLength(byte[] udpBytes) {
        byte[] length = Utilities.getBytesInRange(udpBytes, PACKET_COUNT, LENGTH_SIZE);
        PACKET_COUNT += LENGTH_SIZE;
        return Utilities.bytesToInt(length);
    }

    public String getHeaderChecksum(byte[] udpBytes) {
        byte[] headerChecksum = Utilities.getBytesInRange(udpBytes, PACKET_COUNT, HEADER_CHECKSUM_SIZE);
        PACKET_COUNT += HEADER_CHECKSUM_SIZE;
        return Utilities.bytesToHex(headerChecksum);
    }

    public String getData(byte[] data_row) {
        return Utilities.bytesToHexData(data_row);
    }
}
