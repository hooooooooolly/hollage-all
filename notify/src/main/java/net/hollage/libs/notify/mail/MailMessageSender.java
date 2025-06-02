package net.hollage.libs.notify.mail;

import net.hollage.libs.notify.MessageSender;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.hollage.libs.notify.exception.MessageSendException;

import java.util.Properties;

/**
 * メール送信実装クラス.
 * [How to Use]
 * {@code MailMessageSender} は、指定された SMTP サーバー経由でメールを送信するための実装です。
 * <p>
 * このクラスは {@link MessageSender} インターフェースの一部として利用され、
 * 通知用のメール送信チャネルを提供します。
 * </p>
 *
 * <h2>使用例:</h2>
 * <pre>{@code
 * MailConfig config = new MailConfig(
 *     "smtp.example.com",      // SMTP ホスト
 *     587,                     // ポート
 *     "user@example.com",      // 送信元メールアドレス
 *     "password",              // 認証パスワード
 *     "recipient@example.com"  // 送信先メールアドレス
 * );
 *
 * MessageSender sender = new MailMessageSender(config);
 * sender.sendMessage("テストメッセージ本文");
 * }</pre>
 *
 * <h2>注意事項:</h2>
 * <ul>
 *   <li>JavaMail API がクラスパスに含まれている必要があります（例: {@code jakarta.mail}）</li>
 *   <li>SMTP サーバー設定（TLS、ポート、認証など）によって動作が変わるため、事前に動作確認してください</li>
 *   <li>HTML メールや添付ファイルなど高度な機能には対応していません（プレーンテキストのみ）</li>
 * </ul>
 *
 * @see MessageSender
 * @see MailConfig
 * @see <a href="https://developers.google.com/workspace/gmail/api/quickstart/java?hl=ja">Gmail API</a>
 */
public class MailMessageSender implements MessageSender {

    /** メールコンフィグ. */
    private final MailConfig config;

    /**
     * コンストラクタ.
     *
     * @param config メールコンフィグ
     */
    public MailMessageSender(MailConfig config) {
        this.config = config;
    }

    @Override
    public void sendMessage(String message) throws MessageSendException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.smtpHost());
        props.put("mail.smtp.port", String.valueOf(config.smtpPort()));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.fromAddress(), config.password());
            }
        });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(config.fromAddress()));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.toAddress()));
            mimeMessage.setSubject("通知メッセージ");
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessageSendException("メールの送信に失敗しました", e);
        }
    }
}
