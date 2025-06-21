# net.hollage.libs

Javaプロジェクト向けの個人用ライブラリです。  
Gradleのローカルリポジトリにインストールすることを想定しています。

このライブラリは、AWS SDK v2を用いた各種AWSサービスの操作を簡略化することを目的として、  
AWS周りを中心に各種メッセージ通知用の機能をパッケージごとにまとめています。

----

## ライブラリ一覧

詳しい使い方については各機能のREADME.mdを参照してください。

| ライブラリ名                                                             | パッケージ                                        | 説明                                 |
|--------------------------------------------------------------------|----------------------------------------------|------------------------------------|
| [`aws-eventbridge`](aws-eventbridge/README.md)                     | `net.hollage.libs.aws.eventbridge`           | EventBridge へのカスタムイベント送信           |
| [`aws-eventbridge-scheduler`](aws-eventbridge-scheduler/README.md) | `net.hollage.libs.aws.eventbridge.scheduler` | EventBridge Scheduler によるジョブ作成     |
| [`aws-http`](aws-http/README.md)                                   | `net.hollage.libs.aws.http`                  | AWS SDK v2を用いたHTTP通信をサポート          |
| [`aws-s3`](aws-s3/README.md)                                       | `net.hollage.libs.aws.s3`                    | S3へのUL/DL/一覧取得を行う                  |
| [`aws-sqs`](aws-sqs/README.md)                                     | `net.hollage.libs.aws.sqs`                   | SQS 送受信の簡略化                        |
| [`notify`](notify/README.md)                                       | `net.hollage.libs.notify`                    | Slack・Discord・LINEなどへの通知共通インターフェース |

----

## 導入方法

### インストール

`./gradlew publishToMavenLocal`を実行することで、ローカルのMavenリポジトリにインストールされます。   
インストール先は、通常`~/.m2/repository/net/hollage/libs/`となります。

### 依存関係の追加

各アプリの`build.gradle`に利用したいライブラリの依存関係を追加してください。

```groovy:build.gradle
dependencies {
    implementation 'net.hollage.libs:aws-eventbridge:{$latest}'
    implementation 'net.hollage.libs:aws-eventbridge-scheduler:{$latest}'
    implementation 'net.hollage.libs:aws-http:{$latest}'
    implementation 'net.hollage.libs:aws-s3:{$latest}'
    implementation 'net.hollage.libs:aws-sqs:{$latest}'
    implementation 'net.hollage.libs:notify:{$latest}'
}
```

----

## 今後追加予定、確認予定の機能

- [ ] README.mdの整備  
  →各ライブラリのREADME.mdを整備して、使い方を明確にする予定です。
- [ ] aws-eventbridgeの動作確認
- [ ] notify.mailの動作不備修正  
  →Gmailで上手く送れないことを確認しています。
- [ ] aws-dynamodbの作成
- [ ] aws-snsの作成
- [ ] aws-kinesisの作成
- [ ] コミットメッセージのフォーマットを統一する  
  →v1.0.0リリース前までには明文化予定です。
- [ ] ブランチ戦略の検討  
  →v1.0.0リリースまでには明文化予定です。  
  GitHub Flowをベースに考えています。

### 完了した機能（その内消えます）

- [x] hollage-allとしていたライブラリの分割  
  →AWS周りのライブラリをallとして読み込むと利用側のJarが肥大化するため
- [x] aws-eventbridge-schedulerの動作確認
- [x] aws-s3の動作確認
- [x] aws-sqsの作成

----

## ライセンス

このプロジェクトは、[MIT License](https://opensource.org/license/mit/)の下でライセンスされています。

----

## 補足

### 開発環境

- Amazon Corretto 21.0.6
- Gradle 8.10.2
- IntelliJ IDEA 2025.1.2 (Community Edition)
- Windows 11 Home 64bit

### 備考

プロダクト、テストコード作成においてGitHub Copilotを始めとしたAI生成の力を大いに借りています。  
コントリビューションは歓迎します。  
