package net.hollage.libs.notify.slack;

import net.hollage.libs.notify.exception.MessageSendException;
import net.hollage.libs.notify.exception.MessageSendRuntimeException;
import net.hollage.libs.notify.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackMessageSenderTest {
    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/TEST/TEST/TEST";
    private static final String TEST_MESSAGE = "Test message";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpURLConnection mockConnection;

    private SlackMessageSender sender;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        sender = new SlackMessageSender(WEBHOOK_URL, mockHttpClient);
        outputStream = new ByteArrayOutputStream();
        when(mockHttpClient.createConnection(anyString())).thenReturn(mockConnection);
        when(mockConnection.getOutputStream()).thenReturn(outputStream);
    }

    @Test
    void sendMessage_Success() throws Exception {
        // Arrange
        when(mockConnection.getResponseCode()).thenReturn(200);

        // Act
        assertDoesNotThrow(() -> sender.sendMessage(TEST_MESSAGE));

        // Assert
        String expectedPayload = "{\"text\": \"Test message\"}";
        assertEquals(expectedPayload, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

        verify(mockConnection).setRequestMethod("POST");
        verify(mockConnection).setDoOutput(true);
        verify(mockConnection).setRequestProperty("Content-Type", "application/json");
    }

    @Test
    void sendMessage_WithSpecialCharacters() throws Exception {
        // Arrange
        when(mockConnection.getResponseCode()).thenReturn(200);
        String message = "Test \"quoted\" message";

        // Act
        assertDoesNotThrow(() -> sender.sendMessage(message));

        // Assert
        String expectedPayload = "{\"text\": \"Test \\\"quoted\\\" message\"}";
        assertEquals(expectedPayload, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    void sendMessage_HttpError() throws Exception {
        // Arrange
        when(mockConnection.getResponseCode()).thenReturn(400);

        // Act & Assert
        MessageSendRuntimeException exception = assertThrows(
                MessageSendRuntimeException.class,
                () -> sender.sendMessage(TEST_MESSAGE)
        );

        assertTrue(exception.getMessage().contains("HTTP error code: 400"));
    }

    @Test
    void sendMessage_IoException() throws Exception {
        // Arrange
        when(mockConnection.getOutputStream()).thenThrow(new IOException("Network error"));

        // Act & Assert
        MessageSendException exception = assertThrows(
                MessageSendException.class,
                () -> sender.sendMessage(TEST_MESSAGE)
        );

        assertEquals("Network error", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyUrl() throws Exception {
        // Arrange
        SlackMessageSender sender = new SlackMessageSender("", mockHttpClient);
        when(mockConnection.getOutputStream()).thenThrow(new IOException("Invalid URL"));

        // Act & Assert
        MessageSendException exception = assertThrows(
                MessageSendException.class,
                () -> sender.sendMessage(TEST_MESSAGE)
        );

        assertEquals("Invalid URL", exception.getMessage());
    }
}
