package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpClient {
    HttpURLConnection createConnection(String urlString) throws IOException;
}
