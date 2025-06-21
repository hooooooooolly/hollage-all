# aws-eventbridge

## 概要

AWS EventBridgeとの連携を簡単に行うための Java用ユーティリティです。  
EventBridgeにイベントを送信するためのシンプルなクライアントを提供します。  
EventBridge Schedulerイベントの作成については[aws-eventbridge-scheduler](aws-eventbridge-scheduler/README.md)を参照してください。

## インストール

README.mdファイルを参照してください。

## 使用方法

### EventBridgeにイベントを送信する

以下のスニペットでは1時間後に東京リージョンのmy-function Lambda関数に`{"key": "value"}`
のペイロードを送信するイベントを作成します。  
コンストラクタでRegionを指定しない場合`ap-northeast-1`をデフォルト値として利用します。

```
String name = "my-eventbridge-event";
String groupName = "my-eventbridge-group";
OffsetDateTime runAt = OffsetDateTime.now().plusHours(1);
String arn = "arn:aws:lambda:us-east-1:123456789012:function:my-function";
String roleArn = "arn:aws:iam::123456789012:role/my-role";
String json = "{\"key\":\"value\"}";

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

#### 例外

- `IllegalArgumentException`: 引数が不正な場合にスローされます。
- `RuntimeException`: AWS SDKの呼び出しに失敗した場合にスローされます。
