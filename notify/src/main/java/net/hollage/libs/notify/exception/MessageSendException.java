package net.hollage.libs.notify.exception;

/** メッセージ送信時の例外クラス. */
public class MessageSendException extends Exception {

    /**
     * コンストラクタ.
     *
     * @param message 例外メッセージ
     */
    public MessageSendException(String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message 例外メッセージ
     * @param cause 例外原因
     */
    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
