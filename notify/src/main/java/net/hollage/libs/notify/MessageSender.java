package net.hollage.libs.notify;

import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;

/** メッセージ送信用インターフェース */
public interface MessageSender {

    /**
     * メッセージを送信します.
     *
     * @param message 送信するメッセージ内容
     * @throws MessageSendException メッセージ送信時にIOエラーが発生した場合
     * @throws MessageSendRuntimeException HTTPエラーが発生した場合
     */
    void sendMessage(String message) throws MessageSendException, MessageSendRuntimeException;
}

