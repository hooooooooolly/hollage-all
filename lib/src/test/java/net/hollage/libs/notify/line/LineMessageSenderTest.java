package net.hollage.libs.notify.line;

import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.HttpClient;
import net.hollage.libs.notify.line.LineMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineMessageSenderTest {
    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_MESSAGE = "テストメッセージ";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpURLConnection mockConnection;

    private LineMessageSender sender;

    @BeforeEach
    void setUp() throws IOException {
        sender = new LineMessageSender(TEST_TOKEN, mockHttpClient);
        when(mockHttpClient.createConnection(anyString())).thenReturn(mockConnection);
    }

    @Test
    void sendMessage_shouldThrowMessageSendExceptionOnIOError() throws Exception {
        // Arrange
        when(mockConnection.getOutputStream()).thenThrow(new IOException("Connection failed"));

        // Act & Assert
        MessageSendException exception = assertThrows(
                MessageSendException.class,
                () -> sender.sendMessage(TEST_MESSAGE)
        );

        assertEquals("Connection failed", exception.getMessage());
    }

    @Test
    void sendMessage_shouldThrowMessageSendRuntimeExceptionOnHttpError() throws Exception {
        // Arrange
        when(mockConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockConnection.getResponseCode()).thenReturn(400);

        // Act & Assert
        MessageSendRuntimeException exception = assertThrows(
                MessageSendRuntimeException.class,
                () -> sender.sendMessage(TEST_MESSAGE)
        );

        assertTrue(exception.getMessage().contains("HTTP error code: 400"));
    }
}