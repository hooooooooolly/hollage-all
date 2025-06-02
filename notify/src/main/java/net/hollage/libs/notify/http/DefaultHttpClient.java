package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/** HTTPクライアントのデフォルト実装. */
public class DefaultHttpClient implements HttpClient {

    @Override
    public HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        return (HttpURLConnection) url.openConnection();
    }
}