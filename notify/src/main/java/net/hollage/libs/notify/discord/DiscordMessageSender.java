package net.hollage.libs.notify.discord;

import net.hollage.libs.notify.MessageSender;
import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.DefaultHttpClient;
import net.hollage.libs.notify.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Discordメッセージ送信実装クラス.
 * [How to Use]
 * String webhookUrl = "https://discord.com/api/webhooks/XXXXX/YYYYY";
 * MessageSender discordSender = new DiscordMessageSender(webhookUrl);
 * discordSender.sendMessage("Discordへのテストメッセージです");
 */
public class DiscordMessageSender implements MessageSender {

    /** WebHookURL. */
    private final String webhookUrl;
    /** HttpClient */
    private final HttpClient httpClient;

    /**
     * コンストラクタ.
     *
     * @param webhookUrl WebHookURL
     */
    public DiscordMessageSender(String webhookUrl) {
        this.webhookUrl = Objects.requireNonNull(webhookUrl, "webhookUrl must not be null");
        this.httpClient = new DefaultHttpClient();
    }

    /**
     * コンストラクタ.
     *
     * @param webhookUrl WebHookURL
     * @param httpClient HttpClient
     */
    public DiscordMessageSender(String webhookUrl, HttpClient httpClient) {
        this.webhookUrl = Objects.requireNonNull(webhookUrl, "webhookUrl must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
    }

    @Override
    public void sendMessage(String message) throws MessageSendException, MessageSendRuntimeException {
        try {
            HttpURLConnection conn = httpClient.createConnection(webhookUrl);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String payload = "{\"content\": \"" + escapeJson(message) + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 204) {
                throw new MessageSendRuntimeException("Discord webhook failed. HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            throw new MessageSendException(e.getMessage());
        }
    }

    /**
     * 簡易エスケープ.
     *
     * @param input エスケープ前文字列
     * @return エスケープ後文字列
     */
    private String escapeJson(String input) {
        return input.replace("\"", "\\\""); // 簡易エスケープ
    }

    /**
     * コネクションを生成する.
     *
     * @param url URL
     * @return コネクション
     * @throws IOException 入出力例外
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
