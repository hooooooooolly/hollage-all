package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class DefaultHttpClient implements HttpClient {
    @Override
    public HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }
}