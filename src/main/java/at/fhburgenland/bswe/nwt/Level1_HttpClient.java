package at.fhburgenland.bswe.nwt;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level1_HttpClient {

    private static final int EXPECTED_CLICKS = 1000;
    private static HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        String url = "http://localhost:8848";
        int clickCount = 0; //Zähler für die Anzahl der Klicks
        String currentUrl = url; //Aktuelle URL, die sich ändert

        try {
            while (clickCount <= EXPECTED_CLICKS) {
                // Sendet eine GET-Anfrage an die aktuelle URL und erhält den Antworttext
                String responseText = sendGetRequest(currentUrl).get();
                // Liest den Link für den nächsten Klick aus dem Antworttext
                String nextLinkFromText = readLinkFromText(responseText, currentUrl);
                if (nextLinkFromText == null) {
                    System.out.println("Kein Link wurde gefunden. Beende.");
                    break;
                }
                // Setzt die aktuelle URL auf den gefundenen nächsten Link
                System.out.println("Geklickt auf: " + nextLinkFromText);
                currentUrl = nextLinkFromText;
                clickCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode zum Senden einer asynchronen GET-Anfrage
     * Ich soll CompletableFuture zu verwenden
     * https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
     */
    private static CompletableFuture<String> sendGetRequest(String url) {
        // Erstellt eine HttpRequest für die angegebene URL
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // Setzt die URI für die Anfrage
                .GET() // Legt die Methode der Anfrage fest (GET)
                .build();
        // Sendet die asynchrone Anfrage und verarbeitet die Antwort, wenn sie verfügbar ist
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    // Überprüft den Statuscode der Antwort, 200 = Serverantwort erfolgreich
                    if (response.statusCode() != 200) {
                        // Falls der Statuscode nicht 200 ist, wird eine Exception geworfen
                        throw new RuntimeException("Http-Fehler: " + response.statusCode());
                    }
                    // Gibt den Text des Antwortkörpers zurück
                    return response.body();
                });
    }


    /**
     * Methode liest den HTML-Text und sucht nach Link
     */
    private static String readLinkFromText(String responseText, String baseUrl) {
        Pattern pattern = Pattern.compile("<a\\s+href=\"(.*?)\">");
        //Erstellt einen Matcher für das gegebene Muster
        Matcher matcher = pattern.matcher(responseText);
        if (matcher.find()) {
            String foundLink = matcher.group(1);
            try {
                URI baseUri = new URI(baseUrl);
                URI nextUri = baseUri.resolve(foundLink);
                // Gibt die vollständige URL des nächsten links zurück
                return nextUri.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
