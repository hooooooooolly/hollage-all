package net.hollage.libs.notify.line;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import net.hollage.libs.notify.MessageSender;
import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.DefaultHttpClient;
import net.hollage.libs.notify.http.HttpClient;

/**
 * このクラスは、LINEメッセージ送信用の実装です.
 *
 * <p>送信先IDはLINE IDではなく、LINE Messaging APIから取得したユーザIDやグループIDです.
 *
 * <pre>body.events.source.userId</pre>
 *
 * <h2>使用例</h2>
 *
 * <pre>{@code
 * String accessToken = "INPUT_YOUR_CHANNEL_ACCESS_TOKEN";
 * String to = "INPUT_TARGET_USER_ID_OR_GROUP_ID";
 *
 * LineMessageSender sender = new LineMessageSender(accessToken, to);
 * sender.sendMessage("send test message");
 * }</pre>
 *
 * @see <a href="https://developers.line.biz/ja/docs/messaging-api/sending-messages/">LINE
 *     Developers</a>
 */
public class LineMessageSender implements MessageSender {

  /** LINE APIのURL */
  private static final String LINE_API_URL = "https://api.line.me/v2/bot/message/push";

  /** プッシュメッセージのペイロードテンプレート */
  private static final String PUSH_PAYLOAD_TEMPLATE =
      "{\"to\":\"%s\",\"messages\":[{\"type\":\"text\",\"text\":\"%s\"}]}";

  /** リプライメッセージのペイロードテンプレート */
  private static final String REPLY_PAYLOAD_TEMPLATE =
      "{\"replyToken\":\"%s\",\"messages\":[{\"type\":\"text\",\"text\":\"%s\"}]}";

  /** LINE Messaging APIのチャンネルアクセストークン. */
  private final String channelAccessToken;

  /** 送信先のID. */
  private final String to;

  /** HTTPクライアント. */
  private final HttpClient httpClient;

  /**
   * デフォルトHTTPクライアントを使う場合のコンストラクタ.
   *
   * @param channelAccessToken LINE Messaging APIのチャンネルアクセストークン
   * @param to 送り先のユーザIDやグループID
   */
  public LineMessageSender(String channelAccessToken, String to) {
    this.channelAccessToken = channelAccessToken;
    this.to = to;
    this.httpClient = new DefaultHttpClient();
  }

  /**
   * アレンジHTTPクライアントを使う場合のコンストラクタ.
   *
   * @param channelAccessToken LINE Messaging APIのチャンネルアクセストークン
   * @param to 送り先のユーザIDやグループID
   * @param httpClient デフォルト以外のHTTPクライアント
   */
  public LineMessageSender(String channelAccessToken, String to, HttpClient httpClient) {
    this.channelAccessToken = channelAccessToken;
    this.to = to;
    this.httpClient = httpClient;
  }

  @Override
  public void sendMessage(String message) {
    try {
      HttpURLConnection connection = httpClient.createConnection(LINE_API_URL);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", "Bearer " + channelAccessToken);

      String jsonPayload = String.format(PUSH_PAYLOAD_TEMPLATE, to, escapeJson(message));

      try (OutputStream os = connection.getOutputStream()) {
        os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
        os.flush();
      }

      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        throw new MessageSendRuntimeException(
            "Failed to send message to LINE: HTTP " + responseCode);
      }
    } catch (Exception e) {
      throw new MessageSendRuntimeException("LINE メッセージの送信に失敗しました。" + e.getMessage(), e);
    }
  }

  /**
   * 受信メッセージに対してのリプライメッセージを送信します.
   *
   * @param message 送信するメッセージ内容
   * @param replyToken リプライトークン
   * @throws MessageSendException メッセージ送信時にIOエラーが発生した場合
   * @throws MessageSendRuntimeException HTTPエラーが発生した場合
   */
  public void replyMessage(String message, String replyToken)
      throws MessageSendException, MessageSendRuntimeException {
    try {
      HttpURLConnection connection = httpClient.createConnection(LINE_API_URL);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", "Bearer " + channelAccessToken);

      String jsonPayload = String.format(REPLY_PAYLOAD_TEMPLATE, replyToken, escapeJson(message));

      try (OutputStream os = connection.getOutputStream()) {
        os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
        os.flush();
      }

      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        throw new MessageSendRuntimeException(
            "Failed to send message to LINE: HTTP " + responseCode);
      }
    } catch (Exception e) {
      throw new MessageSendRuntimeException("LINE メッセージの送信に失敗しました。" + e.getMessage(), e);
    }
  }

  /**
   * jsonのエスケープを行います.
   *
   * @param text エスケープしたいテキスト
   * @return エスケープ後のjsonテキスト
   */
  private String escapeJson(String text) {
    return text.replace("\"", "\\\"");
  }
}
