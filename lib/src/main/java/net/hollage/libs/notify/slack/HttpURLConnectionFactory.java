package net.hollage.libs.notify.slack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/** URLコネクションファクトリのインターフェース. */
public interface HttpURLConnectionFactory {
    /**
     * URLコネクションを生成する.
     * @param url URL
     * @return コネクション
     * @throws IOException コネクション確立エラー
     */
    HttpURLConnection create(URL url) throws IOException;
}
