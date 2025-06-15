package net.hollage.libs.aws.http;

/**
 * ContentTypeは、HTTPリクエストのコンテンツタイプを定義する列挙型です。<br>
 * 各コンテンツタイプは、HTTPヘッダーで使用されるMIMEタイプを表します。
 */
public enum ContentType {
  // application属性
  JSON("application/json"),
  JAVA_SCRIPT("application/javascript"),
  OCTET_STREAM("application/octet-stream"),
  // text属性
  TEXT_PLAIN("text/plain"),
  TEXT_CSV("text/csv"),
  TEXT_HTML("text/html"),
  // image属性
  MULTIPART_FORM_DATA("multipart/form-data");

  private final String value;

  ContentType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
