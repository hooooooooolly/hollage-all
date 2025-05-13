package net.hollage.libs.notify.line;

import net.hollage.libs.notify.MessageSender;
import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.DefaultHttpClient;
import net.hollage.libs.notify.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * LINE送信実装クラス.
 * [How to Use]
 * String token = "YOUR_LINE_NOTIFY_TOKEN";
 * MessageSender lineSender = new LineMessageSender(token);
 * lineSender.sendMessage("これはLINE Notifyからのテストメッセージです");
 */
public class LineMessageSender implements MessageSender {

    /** アクセストークン. */
    private final String accessToken;
    private final HttpClient httpClient;
    private static final String LINE_API_URL = "https://notify-api.line.me/api/notify";

    /**
     * コンストラクタ.
     *
     * @param accessToken アクセストークン
     */
    public LineMessageSender(String accessToken) {
        this(accessToken, new DefaultHttpClient());
    }

    // テスト用コンストラクタ
    LineMessageSender(String accessToken, HttpClient httpClient) {
        this.accessToken = accessToken;
        this.httpClient = httpClient;
    }

    @Override
    public void sendMessage(String message) throws MessageSendException, MessageSendRuntimeException {
        try {
            HttpURLConnection conn = httpClient.createConnection(LINE_API_URL);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new MessageSendRuntimeException("LINE Notify failed. HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            throw new MessageSendException(e.getMessage());
        }
    }
}
