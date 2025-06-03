package net.hollage.libs.notify.slack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/** URLコネクションファクトリのデフォルト実装. */
public class DefaultHttpURLConnectionFactory implements HttpURLConnectionFactory {

    @Override
    public HttpURLConnection create(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
