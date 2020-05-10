import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import packetanalyzer.util.Utilities;


public class packetanalyzer {
    /**
     * The main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        String binFile = "";

        if (args.length == 1) {
            binFile = args[0];
        } else {
            System.out.print("Enter binary fine path: ");
            Scanner scanner = new Scanner(System.in);
            binFile = scanner.nextLine();
        }

        if (binFile.equals("")) {
            System.out.println("No binary file path provided.");
            System.out.println("Usage: java packetanalyzer binary_file_path");
            System.exit(0);
        }

        try {
            // Read the file
            Path binFilePath = Paths.get(binFile);
            byte[] fileContents = Files.readAllBytes(binFilePath);
            int bytePointer = 0;

            // print the file in hex.
            Utilities.bytesToHex(fileContents);
            //Ethernet
            Ethernet eth = new Ethernet(fileContents);
            byte[] ethernetBytes = new byte[eth.ETHERNET_SIZE];
            ethernetBytes = Utilities.getBytesInRange(fileContents, bytePointer, eth.ETHERNET_SIZE);
            eth.printEthernetData(ethernetBytes);
            bytePointer += eth.ETHERNET_SIZE;

            byte ip_ver_size = fileContents[bytePointer];
            int protocol = 0;
            // ipv4
            if ((ip_ver_size >>> 4 & 0xFF) == 4) {
                IPv4 ipv4 = new IPv4(bytePointer);
                int header_len = (ip_ver_size & 0xF);
                if (header_len == 0) {
                    header_len = 5;
                }
                ipv4.PACKET_SIZE = (byte) (header_len * ipv4.HEADER_LENGTH_SIZE);
                byte[] ipv4Bytes = Utilities.getBytesInRange(fileContents, bytePointer, ipv4.PACKET_SIZE);
                ipv4.printIPv4Data(ipv4Bytes);
                bytePointer += ipv4.PACKET_SIZE;

                protocol = ipv4.PROTOCOL;

            } else {
                // ipv6
            }
            //ICMP
            if (protocol == 1) {
                ICMP icmp = new ICMP();
                byte[] icmpBytes = Utilities.getBytesInRange(fileContents, bytePointer, icmp.PACKET_SIZE);
                icmp.printICMPData(icmpBytes);
                bytePointer += icmp.PACKET_SIZE;
            }
            //TCP
            else if (protocol == 6) {
                TCP tcp = new TCP();
                byte[] tcpBytes = Utilities.getBytesInRange(fileContents, bytePointer, fileContents.length - bytePointer);
                tcp.DATA_SIZE = fileContents.length - bytePointer;
                tcp.printTCPData(tcpBytes);
                bytePointer += (fileContents.length - bytePointer);
            }
            //UDP
            else if (protocol == 17) {
                UDP udp = new UDP();
                byte[] udpBytes = Utilities.getBytesInRange(fileContents, bytePointer, fileContents.length - bytePointer);
                udp.DATA_SIZE = (byte) (fileContents.length - bytePointer);
                udp.printUDPData(udpBytes);
                bytePointer += (fileContents.length - bytePointer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

