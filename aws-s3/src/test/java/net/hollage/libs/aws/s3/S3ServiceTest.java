package net.hollage.libs.aws.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private S3ClientWrapper clientMock;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        clientMock = mock(S3ClientWrapper.class);
        s3Service = new DefaultS3Service(clientMock);
    }

    @Test
    void testUploadDelegatesToClientWrapper() {
        File file = new File("test.txt");
        s3Service.upload("my-bucket", "key/path/test.txt", file);
        verify(clientMock).upload("my-bucket", "key/path/test.txt", file);
    }

    @Test
    void testDownloadReturnsExpectedResponse() {
        String bucket = "my-bucket";
        String key = "file.txt";
        ResponseInputStream<GetObjectResponse> mockStream = mock(ResponseInputStream.class);

        when(clientMock.download(bucket, key)).thenReturn(mockStream);

        ResponseInputStream<GetObjectResponse> result = s3Service.download(bucket, key);
        assertEquals(mockStream, result);
        verify(clientMock).download(bucket, key);
    }

    @Test
    void testListKeysReturnsExpectedList() {
        String bucket = "my-bucket";
        String prefix = "folder/";
        List<String> expectedKeys = List.of("folder/file1.txt", "folder/file2.txt");

        when(clientMock.listKeys(bucket, prefix)).thenReturn(expectedKeys);

        List<String> actualKeys = s3Service.listKeys(bucket, prefix);
        assertEquals(expectedKeys, actualKeys);
        verify(clientMock).listKeys(bucket, prefix);
    }
}
