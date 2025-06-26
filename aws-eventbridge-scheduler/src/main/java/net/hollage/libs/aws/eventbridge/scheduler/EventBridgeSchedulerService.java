package net.hollage.libs.aws.eventbridge.scheduler;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.*;
import software.amazon.awssdk.utils.StringUtils;

/**
 * EventBridge Scheduler用ラッパー.
 *
 * <pre>{@code
 * // 使用例
 * String name = "my-scheduler-job-id-001";
 * String groupName = "group-name";
 * OffsetDateTime runAt = Instant.parse("2025-01-23T12:34:45Z").atOffset(ZoneOffset.UTC);
 * String arn = "arn:aws:lambda:ap-northeast-1:123456789012:function:my-lambda-function";
 * String roleArn = "arn:aws:iam::123456789012:role/service-role/Amazon_EventBridge_Scheduler_LAMBDA_1234567890";
 * String json = "{\"message\":\"こんにちは\",\"type\":\"reminder\"}";
 *
 * EventBridgeSchedulerService schedulerService = new EventBridgeSchedulerService();
 * schedulerService.scheduleOneTimeEvent(name, groupName, runAt, arn, roleArn, json);
 * }</pre>
 */
public class EventBridgeSchedulerService {

  /** UTCタイムゾーンでの日付時刻のフォーマッター. */
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  /** SchedulerClient. */
  private final SchedulerClient schedulerClient;

  /** 東京リージョンの場合のコンストラクタ. */
  public EventBridgeSchedulerService() {
    this.schedulerClient =
        SchedulerClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
  }

  /**
   * 任意のリージョンの場合のコンストラクタ.
   *
   * @param region リージョン
   */
  public EventBridgeSchedulerService(Region region) {
    this.schedulerClient =
        SchedulerClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
  }

  /**
   * 一度限りのスケジュールイベントを作成します。
   *
   * @param name EventBridgeスケジュール名
   * @param groupName スケジュールグループ名（デフォルト値: default）
   * @param instant 発火時刻
   * @param targetArn 対象となるLambda関数などのARN
   * @param roleArn イベント実行時に使用するIAMロールのARN
   * @param payload Lambdaなどに渡すJSON形式の入力データ
   */
  public void scheduleOneTimeEvent(
      String name,
      String groupName,
      Instant instant,
      String targetArn,
      String roleArn,
      String payload) {
    OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.UTC);
    scheduleOneTimeEvent(name, groupName, offsetDateTime, targetArn, roleArn, payload);
  }

  /**
   * 一度限りのスケジュールイベントを作成します。
   *
   * @param name EventBridgeスケジュール名
   * @param groupName スケジュールグループ名（デフォルト値: default）
   * @param offsetDateTime UTCタイムゾーンでの発火時刻（ISO8601形式で自動整形されます）
   * @param targetArn 対象となるLambda関数などのARN
   * @param roleArn イベント実行時に使用するIAMロールのARN
   * @param payload Lambdaなどに渡すJSON形式の入力データ
   */
  public void scheduleOneTimeEvent(
      String name,
      String groupName,
      OffsetDateTime offsetDateTime,
      String targetArn,
      String roleArn,
      String payload) {
    try {
      String scheduleExpression = "at(" + offsetDateTime.format(DATE_TIME_FORMATTER) + ")";
      CreateScheduleRequest request =
          CreateScheduleRequest.builder()
              .name(name)
              .groupName(StringUtils.isEmpty(groupName) ? "default" : groupName)
              .scheduleExpression(scheduleExpression)
              .flexibleTimeWindow(f -> f.mode(FlexibleTimeWindowMode.OFF))
              .target(
                  t ->
                      t.arn(targetArn)
                          .roleArn(roleArn)
                          .input(payload)
                          .retryPolicy(RetryPolicy.builder().maximumRetryAttempts(1).build()))
              .actionAfterCompletion(ActionAfterCompletion.DELETE)
              .build();

      schedulerClient.createSchedule(request);
    } catch (ResourceNotFoundException e) {
      throw new IllegalArgumentException("指定されたスケジュールグループが存在しません: " + groupName, e);
    } catch (SchedulerException e) {
      throw new RuntimeException("スケジュール作成に失敗しました", e);
    }
  }

  /**
   * 指定した名前、スケジュール式、および Lambda ターゲットを使用してスケジュールを作成します。
   *
   * @param name スケジュール名
   * @param scheduleExpression cron または rate 式（例: rate(5 minutes)）
   * @param lambdaArn 実行対象の Lambda 関数の ARN
   * @param payload Lambda に渡す JSON ペイロード（Map形式）
   * @param roleArn EventBridge Scheduler が使用する IAM ロールの ARN
   */
  public void createSchedule(
      String name,
      String scheduleExpression,
      String lambdaArn,
      Map<String, Object> payload,
      String roleArn) {
    CreateScheduleRequest request =
        CreateScheduleRequest.builder()
            .name(name)
            .scheduleExpression(scheduleExpression)
            .flexibleTimeWindow(
                FlexibleTimeWindow.builder().mode(FlexibleTimeWindowMode.OFF).build())
            .target(
                Target.builder()
                    .arn(lambdaArn)
                    .roleArn(roleArn)
                    .input(SdkUtils.toJson(payload)) // Map を JSON に変換
                    .build())
            .build();

    schedulerClient.createSchedule(request);
  }
}
