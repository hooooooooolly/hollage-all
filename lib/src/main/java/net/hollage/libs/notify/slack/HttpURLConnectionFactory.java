package net.hollage.libs.notify.slack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpURLConnectionFactory {
    HttpURLConnection create(URL url) throws IOException;
}
