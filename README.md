# net.hollage.libs

## 目的

個人用ライブラリ.

## 使い方

```groovy:build.gradle
dependencies {
    implementation 'net.hollage.libs:hollage-all:{$latest}}'
}
```

## 動作環境

- Amazon Corretto 21.0.6
- Gradle 8.10.2
- IntelliJ IDEA 2025.1.1.1 (Community Edition)
- Windows 11 Home 64bit

## パッケージ一覧

### net.hollage.libs.aws

AWSとのやり取りのための機能を提供します.

#### S3

- S3へのアップロード.
- ダウンロード.
- オブジェクトキー一覧の取得.

### net.hollage.libs.notify

MessageSenderインターフェースを実装した、各種メッセージ通知用のパッケージです.

#### discord

#### line

#### mail

#### slack
