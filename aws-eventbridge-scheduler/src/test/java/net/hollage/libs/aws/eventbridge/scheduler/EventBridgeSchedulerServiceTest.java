package net.hollage.libs.aws.eventbridge.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventBridgeSchedulerServiceTest {

    private SchedulerClient mockClient;
    private EventBridgeSchedulerService service;

    @BeforeEach
    void setup() {
        mockClient = mock(SchedulerClient.class);
        // リフレクションや DI 設計変更で SchedulerClient を注入する形にするとより良いですが、
        // 今回は private final を直接使わないためのサブクラス作成で回避
        service = new EventBridgeSchedulerService() {
            {
                // schedulerClient フィールドを上書きする
                try {
                    var field = EventBridgeSchedulerService.class.getDeclaredField("schedulerClient");
                    field.setAccessible(true);
                    field.set(this, mockClient);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void testScheduleOneTimeEvent_withOffsetDateTime() {
        // Arrange
        OffsetDateTime offset = OffsetDateTime.of(2025, 5, 21, 12, 0, 0, 0, ZoneOffset.UTC);
        when(mockClient.createSchedule(any(CreateScheduleRequest.class))).thenReturn(CreateScheduleResponse.builder().build());

        // Act
        service.scheduleOneTimeEvent(
                "test-event",
                "test-group",
                offset,
                "arn:aws:lambda:ap-northeast-1:123456789012:function:my-function",
                "arn:aws:iam::123456789012:role/my-role",
                "{\"message\":\"hello\"}"
        );

        // Assert
        ArgumentCaptor<CreateScheduleRequest> captor = ArgumentCaptor.forClass(CreateScheduleRequest.class);
        verify(mockClient).createSchedule(captor.capture());

        CreateScheduleRequest actualRequest = captor.getValue();
        assertEquals("test-event", actualRequest.name());
        assertEquals("test-group", actualRequest.groupName());
        assertTrue(actualRequest.scheduleExpression().startsWith("at("));
        assertEquals("arn:aws:lambda:ap-northeast-1:123456789012:function:my-function", actualRequest.target().arn());
        assertEquals("{\"message\":\"hello\"}", actualRequest.target().input());
    }

    @Test
    void testScheduleOneTimeEvent_withInstant() {
        Instant instant = Instant.parse("2025-05-21T12:00:00Z");
        when(mockClient.createSchedule(any(CreateScheduleRequest.class))).thenReturn(CreateScheduleResponse.builder().build());

        service.scheduleOneTimeEvent(
                "instant-event",
                "instant-group",
                instant,
                "arn:aws:lambda:ap-northeast-1:123456789012:function:instant-func",
                "arn:aws:iam::123456789012:role/instant-role",
                "{\"msg\":\"instant\"}"
        );

        verify(mockClient).createSchedule(any(CreateScheduleRequest.class));
    }
}
