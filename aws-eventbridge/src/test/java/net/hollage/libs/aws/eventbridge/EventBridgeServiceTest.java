package net.hollage.libs.aws.eventbridge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

import static org.mockito.Mockito.*;

class EventBridgeServiceTest {

    private EventBridgeClient clientMock;
    private DefaultEventBridgeService service;

    @BeforeEach
    void setUp() {
        clientMock = mock(EventBridgeClient.class);
        service = new DefaultEventBridgeService(clientMock);
    }

    @Test
    void sendCustomEvent_success() {
        PutEventsResponse mockResponse = PutEventsResponse.builder()
                .entries(PutEventsResultEntry.builder().eventId("1234").build())
                .build();

        when(clientMock.putEvents(any(PutEventsRequest.class))).thenReturn(mockResponse);

        service.sendCustomEvent("bus", "source", "detailType", "{\"test\":\"value\"}");

        verify(clientMock, times(1)).putEvents(any(PutEventsRequest.class));
    }
}
