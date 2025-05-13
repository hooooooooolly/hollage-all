package net.hollage.libs.notify.mail;

/**
 * {@code MailConfig} は、SMTP 経由でメールを送信するために必要な設定値を保持する構成クラスです。
 *
 * @param smtpHost    SMTPホスト名（例：smtp.example.com）
 * @param smtpPort    SMTPポート（例：587）
 * @param fromAddress 送信元メールアドレス
 * @param password    認証用パスワード
 * @param toAddress   送信先メールアドレス
 */
public record MailConfig(String smtpHost, int smtpPort, String fromAddress, String password, String toAddress) {

    /**
     * コンストラクタ
     * @param smtpHost    SMTPホスト名（例：smtp.example.com）
     * @param smtpPort    SMTPポート（例：587）
     * @param fromAddress 送信元メールアドレス
     * @param password    認証用パスワード
     * @param toAddress   送信先メールアドレス
     */
    public MailConfig {
    }
}
