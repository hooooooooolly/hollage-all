package net.hollage.libs.aws.eventbridge.scheduler;

import java.util.Map;

public class ScheduleTarget {

    private final String arn; // 送信先のリソースARN（例: LambdaのARN）
    private final Map<String, Object> payload; // 送信するJSONデータ

    public ScheduleTarget(String arn, Map<String, Object> payload) {
        this.arn = arn;
        this.payload = payload;
    }

    public String getArn() {
        return arn;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
