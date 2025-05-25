package net.hollage.libs.aws.eventbridge.scheduler;

import java.util.Map;

/**
 * スケジュールの送信先情報クラス.
 */
public class ScheduleTarget {

    /** 送信先のリソースARN（例: LambdaのARN）. */
    private final String arn;
    /** 送信するJSONデータ. */
    private final Map<String, Object> payload;

    /**
     * コンストラクタ.
     *
     * @param arn 送信先のリソースARN（例: LambdaのARN）
     * @param payload 送信するJSONデータ
     */
    public ScheduleTarget(String arn, Map<String, Object> payload) {
        this.arn = arn;
        this.payload = payload;
    }

    /**
     * 送信先のリソースARNを取得する.
     *
     * @return 送信先のリソースARN
     */
    public String getArn() {
        return arn;
    }

    /**
     * 送信するJSONデータを取得する.
     *
     * @return 送信するJSONマップデータ
     */
    public Map<String, Object> getPayload() {
        return payload;
    }
}
