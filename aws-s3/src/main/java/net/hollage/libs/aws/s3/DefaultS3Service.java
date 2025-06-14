package net.hollage.libs.aws.s3;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/** {@link S3Service} のデフォルト実装。 */
public class DefaultS3Service implements S3Service {

  /** S3クライアント用ラッパー. */
  private final S3Client client;

  /**
   * コンストラクタ.
   *
   * @param client S3クライアント用ラッパー
   */
  public DefaultS3Service(S3Client client) {
    this.client = client;
  }

  @Override
  public void upload(String bucket, String key, File file) {
      PutObjectRequest request = PutObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .build();

      client.putObject(request, RequestBody.fromFile(file));
  }

  @Override
  public ResponseInputStream<GetObjectResponse> download(String bucket, String key) {
      GetObjectRequest request = GetObjectRequest.builder()
              .bucket(bucket)
              .key(key)
              .build();

      return client.getObject(request);
  }

  @Override
  public List<String> listKeys(String bucket, String prefix) {
      ListObjectsV2Request request = ListObjectsV2Request.builder()
              .bucket(bucket)
              .prefix(prefix)
              .build();

      ListObjectsV2Response response = client.listObjectsV2(request);

      return response.contents()
              .stream()
              .map(S3Object::key)
              .collect(Collectors.toList());
  }
}
