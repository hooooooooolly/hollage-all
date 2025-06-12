package net.hollage.libs.aws.sqs;

import java.util.List;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

public class DefaultSqsService implements SqsService {
    /** SQSクライアント. */
    private final SqsClient sqsClient;

    /**
     * 東京リージョンのコンストラクタ.
     */
    public DefaultSqsService() {
        this.sqsClient = SqsClient.builder()
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
        this.sqsClient = SqsClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public void sendMessage(String queueUrl, String messageBody) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        sqsClient.sendMessage(request);
    }

    @Override
    public void sendMessage(String queueUrl, String messageBody, Map<String, MessageAttributeValue> attributes) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(attributes)
                .build();
        sqsClient.sendMessage(request);
    }

    @Override
    public List<Message> receiveMessages(String queueUrl, int maxMessages) {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxMessages)
                .build();
        return sqsClient.receiveMessage(request).messages();
    }

    @Override
    public void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest request =  DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();
        sqsClient.deleteMessage(request);
    }
}
