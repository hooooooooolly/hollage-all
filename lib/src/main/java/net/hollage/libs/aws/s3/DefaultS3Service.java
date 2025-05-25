package net.hollage.libs.aws.s3;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.util.List;

/**
 * {@link S3Service} のデフォルト実装。
 */
public class DefaultS3Service implements S3Service {

    /** S3クライアント用ラッパー. */
    private final S3ClientWrapper client;

    /**
     * コンストラクタ.
     *
     * @param client S3クライアント用ラッパー
     */
    public DefaultS3Service(S3ClientWrapper client) {
        this.client = client;
    }

    @Override
    public void upload(String bucket, String key, File file) {
        client.upload(bucket, key, file);
    }

    @Override
    public ResponseInputStream<GetObjectResponse> download(String bucket, String key) {
        return client.download(bucket, key);
    }

    @Override
    public List<String> listKeys(String bucket, String prefix) {
        return client.listKeys(bucket, prefix);
    }
}
