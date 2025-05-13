package net.hollage.libs.notify.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.hollage.libs.notify.exception.MessageSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailMessageSenderTest {
    private static final String SMTP_HOST = "smtp.test.com";
    private static final int SMTP_PORT = 587;
    private static final String FROM_ADDRESS = "from@test.com";
    private static final String TO_ADDRESS = "to@test.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_MESSAGE = "Test Message";

    private MailConfig mockConfig = setupMockMailConfig();
    private Session mockSession = mock(Session.class);
    private MimeMessage mockMimeMessage = mock(MimeMessage.class);

    @Test
    void sendMessage_shouldSendMailSuccessfully() throws Exception {
        // Arrange
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {

            Properties expectedProps = createSmtpProperties(mockConfig);
            sessionMock.when(() -> Session.getInstance(eq(expectedProps), any(Authenticator.class)))
                    .thenReturn(mockSession);

            try (MockedConstruction<MimeMessage> mimeMessageMock = mockConstruction(
                    MimeMessage.class,
                    (mock, context) -> {
                        doNothing().when(mock).setFrom(any(InternetAddress.class));
                        doNothing().when(mock).setRecipients(any(Message.RecipientType.class), any(InternetAddress[].class));
                        doNothing().when(mock).setSubject(anyString());
                        doNothing().when(mock).setText(anyString());
                    })) {

                MailMessageSender mailMessageSender = new MailMessageSender(mockConfig);
                mailMessageSender.sendMessage(TEST_MESSAGE);

                // 検証
                MimeMessage constructedMessage = mimeMessageMock.constructed().get(0);
                verifyMessageConfiguration(constructedMessage, mockConfig);
                transportMock.verify(() -> Transport.send(any(MimeMessage.class)), times(1));
            }
        }
    }

    @Test
    void sendMessage_shouldThrowMessageSendExceptionOnMessagingError() throws Exception {
        // Arrange
        MailConfig mockConfig = setupMockMailConfig();
        Session mockSession = mock(Session.class);

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {

            // Session のモック設定
            Properties expectedProps = createSmtpProperties(mockConfig);
            sessionMock.when(() -> Session.getInstance(eq(expectedProps), any(Authenticator.class)))
                    .thenReturn(mockSession);

            // Transport.send() でエラーをスローするように設定
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("テストエラー"));

            try (MockedConstruction<MimeMessage> mimeMessageMock = mockConstruction(
                    MimeMessage.class,
                    (mock, context) -> {
                        // MimeMessageの基本的な操作は成功するように設定
                        doNothing().when(mock).setFrom(any(InternetAddress.class));
                        doNothing().when(mock).setRecipients(any(Message.RecipientType.class), any(InternetAddress[].class));
                        doNothing().when(mock).setSubject(anyString());
                        doNothing().when(mock).setText(anyString());
                    })) {

                MailMessageSender mailMessageSender = new MailMessageSender(mockConfig);

                // Act & Assert
                MessageSendException exception = assertThrows(
                        MessageSendException.class,
                        () -> mailMessageSender.sendMessage(TEST_MESSAGE)
                );

                assertTrue(exception.getMessage().contains("メールの送信に失敗しました"));
            }
        }
    }

    private MailConfig setupMockMailConfig() {
        return new MailConfig(
                SMTP_HOST,
                SMTP_PORT,
                FROM_ADDRESS,
                TEST_PASSWORD,
                TO_ADDRESS
        );
    }

    private Properties createSmtpProperties(MailConfig config) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.smtpHost());
        props.put("mail.smtp.port", String.valueOf(config.smtpPort()));
        return props;
    }

    private void verifyMessageConfiguration(MimeMessage mockMimeMessage, MailConfig config) throws MessagingException {
        verify(mockMimeMessage).setFrom(new InternetAddress(config.fromAddress()));
        verify(mockMimeMessage).setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(config.toAddress())
        );
        verify(mockMimeMessage).setSubject("通知メッセージ");
        verify(mockMimeMessage).setText(TEST_MESSAGE);
    }

    private void verifyMessageSent(MimeMessage mockMimeMessage, MockedStatic<Transport> transportMock) {
        transportMock.verify(() -> Transport.send(mockMimeMessage), times(1));
    }
}