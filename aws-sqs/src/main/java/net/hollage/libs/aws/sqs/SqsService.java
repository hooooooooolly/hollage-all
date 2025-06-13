package net.hollage.libs.aws.sqs;

import java.util.List;
import java.util.Map;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

/** SqsClientのラッパーインターフェース. */
public interface SqsService {

  /**
   * メッセージを送信する.
   *
   * @param queueUrl 送信先のURL
   * @param messageBody メッセージ本文
   */
  void sendMessage(String queueUrl, String messageBody);

  /**
   * メッセージを送信する.
   *
   * @param queueUrl 送信先のURL
   * @param messageBody メッセージ本文
   * @param attributes ユーザーが指定したメッセージ属性値
   */
  void sendMessage(
      String queueUrl, String messageBody, Map<String, MessageAttributeValue> attributes);

  /**
   * メッセージを受信する.
   *
   * @param queueUrl 受信元のURL
   * @param maxMessages 最大受信数
   * @return 受信したメッセージリスト
   */
  List<ReceiveMessage> receiveMessages(String queueUrl, int maxMessages);

  /**
   * キューからメッセージを削除する.
   *
   * @param queueUrl 削除先URL
   * @param receiptHandle メッセージ受信時に受け取った受信ハンドル
   */
  void deleteMessage(String queueUrl, String receiptHandle);
}
