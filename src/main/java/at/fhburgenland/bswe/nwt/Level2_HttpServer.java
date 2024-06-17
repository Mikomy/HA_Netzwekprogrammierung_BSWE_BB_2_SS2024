package at.fhburgenland.bswe.nwt;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level2_HttpServer {
    public static void main(String[] args) throws IOException {
        int port = 8161;
        /**
         * Erstellt eine neue HttpServer-Instanz, die auf dem angegebenen Port (8161) lauscht.
         * Der zweite Parameter (0) gibt die maximale Anzahl ausstehender Verbindungen an
         * (0 bedeutet, dass das System die optimale Anzahl wählen soll).
         */
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        /**
         Erstellt einen neuen Kontext für den Pfad "/" und weist ihn einem HttpHandler namens MyHandler zu.
         Dieser Handler wird verwendet, um Anfragen zu diesem Pfad zu verarbeiten.
         */
        httpServer.createContext("/", new MyHandler());
        // Setzt den Executor des Servers auf null, dass der Server seinen Standardexecutor verwendet, um Anfragen zu verarbeiten.
        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("Port, auf dem der Server lauscht ");
    }

    /**
     * Definiert eine statische innere Klasse MyHandler, die das HttpHandler-Interface implementiert.
     * Diese Klasse wird verwendet, um HTTP-Anfragen zu verarbeiten.
     */
    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod(); //Ruft die HTTP-Methode der Anfrage ab (z.B. GET, POST).
            if (requestMethod.equalsIgnoreCase("GET")) { //Überprüft, ob die Methode der Anfrage GET ist
                String path = exchange.getRequestURI().getPath(); // Ruft den Pfad der Anfrage-URI ab.
                int sum = calculateSumFromPath(path);
                String response = String.valueOf(sum);
                    // Sendet HTTP-Header als Antwort mit dem Statuscode "200 OK" und der Länge des Antworttextes.
                exchange.sendResponseHeaders(200, response.getBytes().length);
                    // Holt den OutputStream, um den Antworttext zu senden.
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                    // Falls die Anfragemethode nicht GET ist, sendet einen 405 Method Not Allowed Statuscode.
                exchange.sendResponseHeaders(405, 0);
            }
        }

        private int calculateSumFromPath(String path) {
            Pattern pattern = Pattern.compile("/(\\d+)/(\\d+)");
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                int arg1 = Integer.parseInt(matcher.group(1));
                int arg2 = Integer.parseInt(matcher.group(2));
                return arg1 + arg2;
            } else {
                    // Falls das Muster nicht gefunden wird, gibt -1 zurück, um einen ungültigen Pfad anzuzeigen.
                return -1;
            }
        }
    }
}
