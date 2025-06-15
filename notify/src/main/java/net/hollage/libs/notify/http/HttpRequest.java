package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.utils.IoUtils;

/**
 * HttpRequestクラスは、HTTPリクエストを送信するためのユーティリティクラスです。<br>
 * デフォルトではApacheHttpClientを使用してGETおよびPOSTリクエストを送信します。
 */
public class HttpRequest {

  /**
   * デフォルトのHttpClientを使用してHTTPリクエストを送信するためのクラスです。<br>
   * GETおよびPOSTリクエストをサポートしています。
   */
  private final SdkHttpClient httpClient;

  /** デフォルトのApacheHttpClientを使用してHttpRequestを作成します。 */
  public HttpRequest() {
    this.httpClient = ApacheHttpClient.builder().build();
  }

  /**
   * 指定されたHttpClientを使用してHttpRequestを作成します。
   *
   * @param httpClient 使用するSdkHttpClient
   */
  public HttpRequest(SdkHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * GETリクエストを指定されたURLに送信し、レスポンスボディを文字列として返します。
   *
   * @param url GETリクエストの送信先URL
   * @return レスポンスボディの文字列
   * @throws IOException
   */
  public String sendGet(String url) throws IOException {
    SdkHttpFullRequest request =
        SdkHttpFullRequest.builder().method(SdkHttpMethod.GET).uri(URI.create(url)).build();

    try (AbortableInputStream responseStream = send(request)) {
      return IoUtils.toUtf8String(responseStream);
    }
  }

  /**
   * POSTリクエストを指定されたURLに送信し、レスポンスボディを文字列として返します。
   *
   * @param url POSTリクエストの送信先URL
   * @param body リクエストボディの文字列
   * @return レスポンスボディの文字列
   * @throws IOException
   */
  public String sendPost(String url, String body) throws IOException {
    SdkHttpFullRequest request =
        SdkHttpFullRequest.builder()
            .method(SdkHttpMethod.POST)
            .uri(URI.create(url))
            .putHeader("Content-Type", "application/json; charset=UTF-8")
            .contentStreamProvider(ContentStreamProvider.fromString(body, StandardCharsets.UTF_8))
            .build();

    try (AbortableInputStream responseStream = send(request)) {
      return IoUtils.toUtf8String(responseStream);
    }
  }

  /**
   * 指定されたリクエストを送信し、レスポンスボディをAbortableInputStreamとして返します。
   *
   * @param request 送信するリクエスト
   * @return レスポンスボディのAbortableInputStream
   * @throws IOException
   */
  private AbortableInputStream send(SdkHttpFullRequest request) throws IOException {
    HttpExecuteRequest executeRequest =
        HttpExecuteRequest.builder()
            .request(request)
            .contentStreamProvider(request.contentStreamProvider().orElse(null))
            .build();
    HttpExecuteResponse httpExecuteResponse = httpClient.prepareRequest(executeRequest).call();

    return httpExecuteResponse
        .responseBody()
        .orElseThrow(() -> new IOException("No response body received"));
  }
}
