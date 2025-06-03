package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.HttpURLConnection;

/** HTTPクライアントのインターフェース. */
public interface HttpClient {

    /**
     * URLに接続するためのHttpURLConnectionを生成する.
     * @param urlString URL文字列
     * @return HttpURLConnection
     * @throws IOException 接続に失敗した場合
     */
    HttpURLConnection createConnection(String urlString) throws IOException;
}
