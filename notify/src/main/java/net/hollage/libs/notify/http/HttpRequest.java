package net.hollage.libs.notify.http;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import software.amazon.awssdk.http.*;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.utils.IoUtils;

public class HttpRequest {

  private final SdkHttpClient httpClient;

  public HttpRequest() {
    this.httpClient = ApacheHttpClient.builder().build();
  }

  public String sendGet(String url) throws IOException {
    SdkHttpFullRequest request =
        SdkHttpFullRequest.builder().method(SdkHttpMethod.GET).uri(URI.create(url)).build();

    try (AbortableInputStream responseStream = send(request)) {
      return IoUtils.toUtf8String(responseStream);
    }
  }

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
