package net.hollage.libs.aws.s3;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.util.List;

/** S3クライアント用ラッパー. */
public interface S3ClientWrapper {

    /**
     * S3へファイルをアップロードする.
     *
     * @param bucket アップロード先のバケット名
     * @param key    オブジェクトキー
     * @param file   アップロードするファイルオブジェクト
     */
    void upload(String bucket, String key, File file);

    /**
     * S3からファイルをダウンロードする.
     *
     * @param bucket ダウンロード先のバケット名
     * @param key    オブジェクトキー
     * @return ダウンロードしたデータ
     */
    ResponseInputStream<GetObjectResponse> download(String bucket, String key);

    /**
     * オブジェクトキー一覧を取得する.
     *
     * @param bucket 取得先のバケット名
     * @param prefix 取得先のプレフィクス
     * @return 指定したパス以下のオブジェクトキー一覧
     */
    List<String> listKeys(String bucket, String prefix);
}
