package net.hollage.libs.aws.sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

/** SQSクライアントのデフォルト実装. */
public class DefaultSqsService implements SqsService {

  /** SQSクライアント. */
  private final SqsClient sqsClient;

  /**
   * コンストラクタ.<br>
   * SQSクライアントを直接指定する場合に使用.
   *
   * @param sqsClient SQSクライアント
   */
  protected DefaultSqsService(SqsClient sqsClient) {
    this.sqsClient = sqsClient;
  }

  /** 東京リージョンのコンストラクタ. */
  public DefaultSqsService() {
    this.sqsClient =
        SqsClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
  }

  /**
   * コンストラクタ.<br>
   * 東京以外のリージョンを利用したい時に使用.
   *
   * @param region AWSリージョン
   */
  public DefaultSqsService(Region region) {
    this.sqsClient =
        SqsClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
  }

  @Override
  public void sendMessage(String queueUrl, String messageBody) {
    SendMessageRequest request =
        SendMessageRequest.builder().queueUrl(queueUrl).messageBody(messageBody).build();
    sqsClient.sendMessage(request);
  }

  @Override
  public void sendMessage(
      String queueUrl, String messageBody, Map<String, MessageAttributeValue> attributes) {
    SendMessageRequest request =
        SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(messageBody)
            .messageAttributes(attributes)
            .build();
    sqsClient.sendMessage(request);
  }

  @Override
  public List<ReceiveMessage> receiveMessages(String queueUrl, int maxMessages) {
    ReceiveMessageRequest request =
        ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(maxMessages).build();
    List<Message> messageList = sqsClient.receiveMessage(request).messages();
    List<ReceiveMessage> receiveMessages = new ArrayList<>();
    for (Message message : messageList) {
      receiveMessages.add(
          new ReceiveMessage(message.messageId(), message.body(), message.receiptHandle()));
    }
    return receiveMessages;
  }

  @Override
  public void deleteMessage(String queueUrl, String receiptHandle) {
    DeleteMessageRequest request =
        DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build();
    sqsClient.deleteMessage(request);
  }
}
