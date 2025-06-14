package net.hollage.libs.aws.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

class S3ServiceTest {

  private S3Client clientMock;
  private S3Service s3Service;

  @BeforeEach
  void setUp() {
    clientMock = mock(S3Client.class);
    s3Service = new DefaultS3Service(clientMock);
  }

  @Test
  void testUpload() {
    String bucket = "my-bucket";
    String key = "file.txt";
    File file = new File(getClass().getClassLoader().getResource("local.txt").getFile());

    s3Service.upload(bucket, key, file);

    PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
    verify(clientMock).putObject(eq(request), any(RequestBody.class));
  }

  @Test
  void testDownload() {
    String bucket = "my-bucket";
    String key = "file.txt";
    ResponseInputStream<GetObjectResponse> mockStream = mock(ResponseInputStream.class);

    when(clientMock.getObject(any(GetObjectRequest.class))).thenReturn(mockStream);

    ResponseInputStream<GetObjectResponse> result = s3Service.download(bucket, key);
    assertEquals(mockStream, result);
    verify(clientMock).getObject(any(GetObjectRequest.class));
  }

  @Test
  void testListKeys() {
    String bucket = "my-bucket";
    String prefix = "folder/";
    List<String> expectedKeys = List.of("folder/file1.txt", "folder/file2.txt");
    when(clientMock.listObjectsV2(any(ListObjectsV2Request.class)))
        .thenReturn(
            ListObjectsV2Response.builder()
                .contents(
                    List.of(
                        S3Object.builder().key("folder/file1.txt").build(),
                        S3Object.builder().key("folder/file2.txt").build()))
                .build());

    List<String> actualKeys = s3Service.listKeys(bucket, prefix);

    assertEquals(expectedKeys, actualKeys);
    verify(clientMock).listObjectsV2(any(ListObjectsV2Request.class));
  }
}
