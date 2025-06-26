# aws-eventbridge-scheduler

## 概要

EventBridge Schedulerにジョブを作成するためのライブラリです。

## インストール

[README.md](README.md)ファイルを参照してください。

## 使用方法

### EventBridgeにイベントを送信する

以下のスニペットでは  
2025/01/23 12:34:45(UTC)に、  
東京リージョンのmy-lambda-functionに、  
`{"message": "こんにちは", "type": "reminder"}`  
のペイロードを送信します。  
コンストラクタでRegionを指定しない場合`ap-northeast-1`をデフォルト値として利用します。

```
String name = "my-scheduler-job-id-001";
String groupName = "group-name";
OffsetDateTime runAt = Instant.parse("2025-01-23T12:34:45Z").atOffset(ZoneOffset.UTC);
String arn = "arn:aws:lambda:ap-northeast-1:123456789012:function:my-lambda-function";
String roleArn ="arn:aws:iam::123456789012:role/service-role/Amazon_EventBridge_Scheduler_LAMBDA_1234567890";
String json = "{\"message\":\"こんにちは\",\"type\":\"reminder\"}";

EventBridgeSchedulerService schedulerService = new EventBridgeSchedulerService();
schedulerService.scheduleOneTimeEvent(name, groupName, runAt, arn, roleArn, json);
```

#### 引数

- `name`: EventBridgeSchedulerイベントの名前
- `groupName`: EventBridgeSchedulerイベントグループの名前（指定しない場合はdefaultグループが使用されます）
- `runAt`: イベントを実行する日時（`OffsetDateTime`形式または`Instant`形式）
- `arn`: イベントを送信するターゲットのARN（例: Lambda関数のARN）
- `roleArn`: イベントを送信するためのIAMロールのARN
- `json`: イベントのペイロード（JSON形式の文字列）
