package net.hollage.libs.aws.eventbridge;

import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

import java.util.List;

public class DefaultEventBridgeService implements EventBridgeService {

    /** EventBridgeClient. */
    private final EventBridgeClient client;

    /** コンストラクタ. */
    public DefaultEventBridgeService() {
        this(EventBridgeClient.create());
    }

    /**
     * コンストラクタ.
     *
     * @param client EventBridgeClient
     */
    public DefaultEventBridgeService(EventBridgeClient client) {
        this.client = client;
    }

    @Override
    public void sendCustomEvent(String eventBusName, String source, String detailType, String detailJson) {
        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .eventBusName(eventBusName)
                .source(source)
                .detailType(detailType)
                .detail(detailJson)
                .build();

        PutEventsRequest request = PutEventsRequest.builder()
                .entries(entry)
                .build();

        PutEventsResponse response = client.putEvents(request);

        List<PutEventsResultEntry> resultEntries = response.entries();
        for (PutEventsResultEntry resultEntry : resultEntries) {
            if (resultEntry.eventId() == null) {
                throw new RuntimeException("Failed to send event: " + resultEntry);
            }
        }
    }
}
