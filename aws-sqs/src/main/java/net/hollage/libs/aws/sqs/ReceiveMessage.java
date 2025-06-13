package net.hollage.libs.aws.sqs;

/**
 * SQSから受信したメッセージのデータを保持するレコードクラス.
 *
 * @param messageId メッセージID
 * @param body メッセージ本文
 * @param receiptHandle 受信ハンドル
 */
public record ReceiveMessage(String messageId, String body, String receiptHandle) {}
