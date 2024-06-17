package at.fhburgenland.bswe.nwt;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class Level5_UdpPeer {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("localhost");
            // Deklariert ein Byte-Array buf, das für den Datenaustausch verwendet wird.
            byte[] buf;


            String message = "Hello, UDP server!";
            // Konvertiert die Nachricht message in ein Byte-Array buf.
            buf = message.getBytes();
            // Erzeugt ein DatagramPacket
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 22680);
            socket.send(packet);

            // Receive response from the server
            while (true) {
                // Setzt das Byte-Array buf auf eine neue Größe, um neue Daten zu empfangen.
                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                // Wartet, bis ein DatagramPacket vom Server empfangen wird.
                socket.receive(packet);

                // Konvertiert die empfangenen Daten in einen String (received),
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Nachricht erhalten: " + received);

                if (received.startsWith("#")) {
                    continue;
                }
                if (received.equalsIgnoreCase("exit")) {
                    break;
                }

                // Setzt die Nachricht, die zurück an den Server gesendet werden soll (received).
                message = received;
                buf = message.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, 22680);
                socket.send(packet);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

