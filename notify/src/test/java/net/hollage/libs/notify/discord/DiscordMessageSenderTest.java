package net.hollage.libs.notify.discord;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscordMessageSenderTest {

    private static final String TEST_WEBHOOK_URL = "https://discord.com/api/webhooks/test/webhook";
    private static final String TEST_MESSAGE = "テストメッセージ";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpURLConnection mockConnection;

    private DiscordMessageSender sender;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        sender = new DiscordMessageSender(TEST_WEBHOOK_URL, mockHttpClient);
        outputStream = new ByteArrayOutputStream();
        when(mockHttpClient.createConnection(anyString())).thenReturn(mockConnection);
    }

    @Test
    void sendMessage_shouldSendMessageSuccessfully() throws Exception {
        // Arrange
        when(mockConnection.getOutputStream()).thenReturn(outputStream);
        when(mockConnection.getResponseCode()).thenReturn(204);

        // Act
        sender.sendMessage(TEST_MESSAGE);

        // Assert
        verify(mockConnection).setRequestMethod("POST");
        verify(mockConnection).setDoOutput(true);
        verify(mockConnection).setRequestProperty("Content-Type", "application/json");

        String expectedPayload = "{\"content\": \"" + TEST_MESSAGE + "\"}";
        assertEquals(expectedPayload, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void sendMessage_shouldEscapeJsonSpecialCharacters() throws Exception {
        // Arrange
        when(mockConnection.getOutputStream()).thenReturn(outputStream);
        when(mockConnection.getResponseCode()).thenReturn(204);

        String messageWithSpecialChars = "Test \"message\" with quotes";

        // Act
        sender.sendMessage(messageWithSpecialChars);

        // Assert
        String expectedPayload = "{\"content\": \"Test \\\"message\\\" with quotes\"}";
        assertEquals(expectedPayload, outputStream.toString(StandardCharsets.UTF_8));
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
}