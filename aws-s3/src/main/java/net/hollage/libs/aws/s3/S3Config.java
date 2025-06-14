package net.hollage.libs.aws.s3;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * リージョンやクレデンシャルを明示的に指定する場合のコンフィグクラス.<br>
 * リージョンを修正したい場合はコンストラクタに拡張性を持たせる.<br>
 * [How to Use]<br>
 * S3Service s3 = S3Config.createDefaultS3Service();<br>
 * s3.upload("my-bucket", "path/to/file.txt", new File("local.txt"));
 */
public class S3Config {

  /**
   * 東京リージョンでS3クライアントを生成する.
   *
   * @return S3クライアント
   */
  public static S3Service createDefaultS3Service() {
    S3Client s3Client = S3Client.builder().region(Region.AP_NORTHEAST_1).build();
    return new DefaultS3Service(s3Client);
  }

  /**
   * リージョンを明示的に指定したS3クライアントを生成する.
   *
   * @param region リージョン名（例: "ap-northeast-1"）
   * @return S3クライアント
   */
  public static S3Service createDefaultS3Service(String region) {
    S3Client s3Client = S3Client.builder().region(Region.of(region)).build();
    return new DefaultS3Service(s3Client);
  }
}
