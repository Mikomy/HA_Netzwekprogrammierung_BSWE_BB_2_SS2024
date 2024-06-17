package at.fhburgenland.bswe.nwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Level4_TcpServer {
    public static void main(String[] args) {
        int portNumber = 19230;

        /**
         *  Erstellt einen neuen ServerSocket, der auf dem angegebenen Port lauscht.
         */
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Port, auf dem der Server lauscht: " + portNumber);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Neue Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Nachricht von Client: " + inputLine);

                        // Überprüfen, ob die Eingabe mit einem # beginnt, und sie ignorieren
                        if (inputLine.startsWith("#")) {
                            continue;
                        }

                        // Überprüfen, ob die Eingabe "exit" ist, und die Verbindung schließen
                        if (inputLine.equalsIgnoreCase("exit")) {
                            break;
                        }

                        // Senden Sie die empfangene Nachricht zurück an den Client
                        out.println(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


