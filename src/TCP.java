import packetanalyzer.util.Utilities;

/**
 * The TCP class
 */

public class TCP {

//    final byte PACKET_SIZE = 20;
    private static final byte SOURCE_PORT_SIZE = 2;
    private static final byte DESTINATION_PORT_SIZE = 2;
    private static final byte SEQUENCE_NUMBER_SIZE = 4;
    private static final byte ACKNOWLEDGEMENT_NUMBER_SIZE = 4;
    private static final byte DATA_OFFSET_RESERVED_FLAGS_SIZE = 2;

    private static final byte WINDOW_SIZE = 2;
    private static final byte HEADER_CHECKSUM_SIZE = 2;

    private static final byte URGENT_POINTER_SIZE = 2;
//    byte OPTIONS_SIZE = 0;

    int DATA_SIZE;
    int FIRST_64_BYTES_DATA = 64;
    int PACKET_COUNT = 0;


    public void printTCPData(byte[] tcpBytes) {
        System.out.println("TCP:  ------ TCP Header ------     ");
        System.out.println("TCP:  ");

        System.out.println("TCP:  Source port = " + getSourcePort(tcpBytes));
        System.out.println("TCP:  Destination port = " + getDestinationPort(tcpBytes));
        System.out.println("TCP:  Sequence number = " + getSequenceNumber(tcpBytes));
        System.out.println("TCP:  Acknowledgement number = " + getAcknowledgementNumber(tcpBytes));

        byte[] flagAndFragmentOffset = getDataOffsetReservedFlags(tcpBytes);
        String dataOffset = Integer.toString(flagAndFragmentOffset[0] >>> 4 & 0xF);
        System.out.println("TCP:  Data offset = " + dataOffset + " bits -- " + Integer.parseInt(dataOffset)*4 + " Bytes");
        System.out.println("TCP:  Flags  = " + Utilities.byteToHex(flagAndFragmentOffset[0]));

        printFlagAndFragmentOffset(flagAndFragmentOffset[1]);

        System.out.println("TCP:  Window = " + getWindow(tcpBytes));
        System.out.println("TCP:  Checksum = " + getHeaderChecksum(tcpBytes));
        System.out.println("TCP:  Urgent pointer = " + getUrgentPointer(tcpBytes));
        System.out.println("TCP:  No options");
        System.out.println("TCP:  ");

        System.out.println("TCP:  Data: (first 64 bytes)");
        byte[] data = Utilities.getBytesInRange(tcpBytes, 0,  DATA_SIZE);
        int byte_add = 16;

        for (int i = 0; i < FIRST_64_BYTES_DATA && (i <= data.length); i += 16) {
            if (i + byte_add > data.length) {
                byte_add = (data.length - i);
            }
            System.out.println("TCP:  " + getData(Utilities.getBytesInRange(data, i, byte_add)));
        }
    }

    public void printFlagAndFragmentOffset(byte flagAndFragmentOffset) {
        boolean flag = Utilities.checkBits(flagAndFragmentOffset, 5);
        String flagString = flag ? "Urgent pointer" : "No urgent pointer";
        System.out.println("TCP:     .." + (flag ? 1 : 0) + ". ....  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset, 4);
        flagString = flag ? "Acknowledgement" : "No Acknowledgement";
        System.out.println("TCP:     ..." + (flag ? 1 : 0) + " ....  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset, 3);
        flagString = flag ? "Push" : "No Push";
        System.out.println("TCP:     .... " + (flag ? 1 : 0) + "...  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset, 2);
        flagString = flag ? "Reset" : "No Reset";
        System.out.println("TCP:     .... ." + (flag ? 1 : 0) + "..  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset, 1);
        flagString = flag ? "Syn" : "No Syn";
        System.out.println("TCP:     .... .." + (flag ? 1 : 0) + ".  = " + flagString);

        flag = Utilities.checkBits(flagAndFragmentOffset, 0);
        flagString = flag ? "Fin" : "No Fin";
        System.out.println("TCP:     .... ..." + (flag ? 1 : 0) + "  = " + flagString);
    }

    public int getSourcePort(byte[] tcpBytes) {
        byte[] sourcePort = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, SOURCE_PORT_SIZE);
        PACKET_COUNT += SOURCE_PORT_SIZE;
        return Utilities.bytesToInt(sourcePort);
    }

    public int getDestinationPort(byte[] tcpBytes) {
        byte[] destinationPort = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, DESTINATION_PORT_SIZE);
        PACKET_COUNT += DESTINATION_PORT_SIZE;
        return Utilities.bytesToInt(destinationPort);
    }

    public long getSequenceNumber(byte[] tcpBytes) {
        byte[] sequenceNumber = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, SEQUENCE_NUMBER_SIZE);
        PACKET_COUNT += SEQUENCE_NUMBER_SIZE;
        return Utilities.bytesToLong(sequenceNumber);
    }

    public long getAcknowledgementNumber(byte[] tcpBytes) {
        byte[] acknowledgementNumber = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, ACKNOWLEDGEMENT_NUMBER_SIZE);
        PACKET_COUNT += ACKNOWLEDGEMENT_NUMBER_SIZE;
        return Utilities.bytesToLong(acknowledgementNumber);
    }

    public byte[] getDataOffsetReservedFlags(byte[] tcpBytes) {
        byte[] dataOffset = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, DATA_OFFSET_RESERVED_FLAGS_SIZE);
        PACKET_COUNT += DATA_OFFSET_RESERVED_FLAGS_SIZE;
        return dataOffset;
    }

    public int getWindow(byte[] tcpBytes) {
        byte[] window = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, WINDOW_SIZE);
        PACKET_COUNT += WINDOW_SIZE;
        return Utilities.bytesToInt(window);
    }

    public String getHeaderChecksum(byte[] tcpBytes) {
        byte[] headerChecksum = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, HEADER_CHECKSUM_SIZE);
        PACKET_COUNT += HEADER_CHECKSUM_SIZE;
        return Utilities.bytesToHex(headerChecksum);
    }

    public int getUrgentPointer(byte[] tcpBytes) {
        byte[] urgentPointer = Utilities.getBytesInRange(tcpBytes, PACKET_COUNT, URGENT_POINTER_SIZE);
        PACKET_COUNT += URGENT_POINTER_SIZE;
        return Utilities.bytesToInt(urgentPointer);
    }

    public String getData(byte[] data_row) {
        return Utilities.bytesToHexData(data_row);
    }
}
