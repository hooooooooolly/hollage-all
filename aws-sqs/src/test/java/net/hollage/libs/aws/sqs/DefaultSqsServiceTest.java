package net.hollage.libs.aws.sqs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

class DefaultSqsServiceTest {

  private final SqsClient mockedSqsClient = mock(SqsClient.class);
  private final DefaultSqsService sut = new DefaultSqsService(mockedSqsClient);

  @Test
  void sendMessage_正常系() {
    String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/123456789012/MyQueue";
    String messageBody = "Test message";

    assertDoesNotThrow(() -> sut.sendMessage(queueUrl, messageBody));

    verify(mockedSqsClient).sendMessage(any(SendMessageRequest.class));
  }

  @Test
  void sendMessage_属性付き_正常系() {
    String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/123456789012/MyQueue";
    String messageBody = "Test message with attributes";
    Map<String, MessageAttributeValue> attributes =
        Map.of(
            "Attribute1",
                MessageAttributeValue.builder().dataType("String").stringValue("Value1").build(),
            "Attribute2",
                MessageAttributeValue.builder().dataType("Number").stringValue("123").build());

    assertDoesNotThrow(() -> sut.sendMessage(queueUrl, messageBody, attributes));

    verify(mockedSqsClient)
        .sendMessage(
            SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(attributes)
                .build());
  }

  @Test
  void receiveMessages_正常系() {
    String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/123456789012/MyQueue";
    int maxMessages = 5;
    when(mockedSqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
        .thenReturn(
            ReceiveMessageResponse.builder()
                .messages(
                    Message.builder().body("Test message").receiptHandle("receipt-handle").build())
                .build());

    assertDoesNotThrow(() -> sut.receiveMessages(queueUrl, maxMessages));

    // SQSクライアントのメソッドが呼び出されることを確認
    verify(mockedSqsClient).receiveMessage(any(ReceiveMessageRequest.class));
  }

  @Test
  void deleteMessage_正常系() {
    String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/123456789012/MyQueue";
    String receiptHandle = "receipt-handle";

    assertDoesNotThrow(() -> sut.deleteMessage(queueUrl, receiptHandle));

    verify(mockedSqsClient).deleteMessage(any(DeleteMessageRequest.class));
  }
}
