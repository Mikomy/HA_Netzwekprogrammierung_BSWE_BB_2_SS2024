package at.fhburgenland.bswe.nwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Level3_TcpClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 2028;

        /**
         *  Versucht, eine Verbindung zum Server herzustellen,
         *  der unter der angegebenen Adresse und dem Port läuft
         */
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server Message: " + serverMessage);
                if (!serverMessage.startsWith("#")) {
                    out.println(serverMessage);
                    System.out.println("Sent back: " + serverMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
