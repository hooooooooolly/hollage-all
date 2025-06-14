# net.hollage.libs

## 目的

個人用ライブラリ。

## 使い方

```groovy:build.gradle
dependencies {
    implementation 'net.hollage.libs:aws-eventbridge:{$latest}}'
    implementation 'net.hollage.libs:aws-eventbridge-scheduler:{$latest}}'
    implementation 'net.hollage.libs:aws-s3:{$latest}}'
    implementation 'net.hollage.libs:aws-sqs:{$latest}}'
    implementation 'net.hollage.libs:notify:{$latest}}'
}
```

## 確認済み動作環境

- Amazon Corretto 21.0.6
- Gradle 8.10.2
- IntelliJ IDEA 2025.1.2 (Community Edition)
- Windows 11 Home 64bit

## パッケージ一覧

### aws-eventbridge

EventBridgeに任意のJSONを送信します。

### aws-eventbridge-scheduler

EventBridge Schedulerを作成します。

### aws-s3

- S3へのアップロード
- ダウンロード
- オブジェクトキー一覧の取得  
  を行います。

### aws-sqs

- SQSへの送受信
- キューの削除  
  を行います。

### notify

MessageSenderインターフェースを実装した、各種メッセージ通知用のパッケージです。  
以下に対応。

- discord
- line
- mail
- slack

## TODO

- [x] hollage-allとしていたライブラリの分割  
  →AWS周りのライブラリをallとして読み込むと利用側のJarが肥大化するため
- [ ] aws-eventbridgeの動作確認
- [x] aws-eventbridge-schedulerの動作確認
- [x] aws-s3の動作確認
- [ ] notify.mailの動作不備修正  
  →Gmailで上手く送れないことを確認しています。
- [ ] aws-dynamodbの作成
- [x] aws-sqsの作成
- [ ] aws-snsの作成
- [ ] aws-kinesisの作成

## 補足

プロダクト、テストコード作成においてGitHub Copilotを始めとしたAI生成の力を大いに借りています。  
