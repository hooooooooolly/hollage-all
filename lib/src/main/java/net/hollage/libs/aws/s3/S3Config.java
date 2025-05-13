package net.hollage.libs.aws.s3;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * リージョンやクレデンシャルを明示的に指定する場合のコンフィグクラス.<br>
 * リージョンを修正したい場合はコンストラクタに拡張性を持たせる.
 * [How to Use]
 * S3Service s3 = S3Config.createDefaultS3Service();
 * s3.upload("my-bucket", "path/to/file.txt", new File("local.txt"));
 */
public class S3Config {

    /**
     * S3クライアントを生成する.
     *
     * @return S3クライアント
     */
    public static S3Service createDefaultS3Service() {
        S3Client s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_1)
                .build();

        S3ClientWrapper wrapper = new AwsS3ClientWrapper(s3Client);
        return new DefaultS3Service(wrapper);
    }
}
