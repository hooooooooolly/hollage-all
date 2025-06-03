package net.hollage.libs.notify.slack;

import net.hollage.libs.notify.MessageSender;
import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.DefaultHttpClient;
import net.hollage.libs.notify.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Slackメッセージ送信実装クラス.
 * <p>
 * Slackのwebhook URLを使用してメッセージを送信します。
 * </p>
 *
 * 使用例:
 * <pre>{@code
 * String webhookUrl = "https://hooks.slack.com/services/XXXXX/YYYYY/ZZZZZ";
 * MessageSender slackSender = new SlackMessageSender(webhookUrl);
 * slackSender.sendMessage("Slackへのテストメッセージ");
 * }</pre>
 *
 * @since 1.0
 */
public class SlackMessageSender implements MessageSender {

    /** WebHookURL. */
    private final String webhookUrl;
    /** HttpURLConnectionFactory. */
    private final HttpClient httpClient;

    /**
     * コンストラクタ.<br>
     * webhookUrlは必須項目のため、NullPointerExceptionが発生する可能性あり.
     *
     * @param webhookUrl WebHookURL
     */
    public SlackMessageSender(String webhookUrl) {
        this(webhookUrl, new DefaultHttpClient());
    }

    /**
     * テスト用のコンストラクタ.
     *
     * @param webhookUrl WebHookURL
     * @param httpClient HttpClient
     */
    SlackMessageSender(String webhookUrl, HttpClient httpClient) {
        this.webhookUrl = Objects.requireNonNull(webhookUrl, "webhookUrl must not be null");
        this.httpClient = httpClient;

    }

    @Override
    public void sendMessage(String message) throws MessageSendException, MessageSendRuntimeException {
        try {
            HttpURLConnection conn = httpClient.createConnection(webhookUrl);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String payload = "{\"text\": \"" + escapeJson(message) + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new MessageSendRuntimeException("Slack webhook failed. HTTP error code: " + responseCode);
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
        return input.replace("\"", "\\\"");
    }
}

