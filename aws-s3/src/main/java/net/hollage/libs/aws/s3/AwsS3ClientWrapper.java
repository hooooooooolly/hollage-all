package net.hollage.libs.aws.s3;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/** S3クライアント用ラッパー. */
public class AwsS3ClientWrapper implements S3ClientWrapper {

    /** S3クライアント. */
    private final S3Client s3;

    /**
     * コンストラクタ.
     *
     * @param s3 S3クライアント
     */
    public AwsS3ClientWrapper(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public void upload(String bucket, String key, File file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3.putObject(request, RequestBody.fromFile(file));
    }

    @Override
    public ResponseInputStream<GetObjectResponse> download(String bucket, String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3.getObject(request);
    }

    @Override
    public List<String> listKeys(String bucket, String prefix) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();

        ListObjectsV2Response response = s3.listObjectsV2(request);

        return response.contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}
