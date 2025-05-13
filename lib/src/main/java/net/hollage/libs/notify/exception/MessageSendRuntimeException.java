package net.hollage.libs.notify.exception;

/** メッセージ送信時の実行時例外クラス. */
public class MessageSendRuntimeException extends RuntimeException {

    /**
     * コンストラクタ.
     *
     * @param message 例外メッセージ
     */
    public MessageSendRuntimeException(String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message 例外メッセージ
     * @param cause 例外原因
     */
    public MessageSendRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
