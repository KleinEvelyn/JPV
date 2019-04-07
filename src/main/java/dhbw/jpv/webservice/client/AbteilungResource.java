package dhbw.jpv.webservice.client;

        
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AbteilungResource {
    public static final String URL = "";

    public String url = URL;
    public String username = "";
    public String password = "";

    public AbteilungResource() {
    }

    public AbteilungResource(String url) {
        this.url = url;
    }
    /*
    public Abteilung[] findAbteilung(String query) throws UnirestException, WebAppException {
        // HTTP-Anfrage senden
        HttpResponse<String> httpResponse = Unirest.get(this.url)
                .queryString("query", query)
                .header("accept", "application/json")
                .asString();

        // Exception werfen, wenn der Server einen Fehler meldet
        try {
            ExceptionResponse er = this.gson.fromJson(httpResponse.getBody(), ExceptionResponse.class);

            if (er.exception != null) {
                throw new WebAppException(er);
            }
        } catch (JsonSyntaxException ex) {
            // Okay, keine Fehlerdaten empfangen
        }

        // Antwortdaten zurückgeben
        return this.Gson.fromJson(httpResponse.getBody(), Abteilung[].class);
    }*/
}