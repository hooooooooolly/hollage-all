package net.hollage.libs.aws.eventbridge;

/**
 * AWS EventBridge にカスタムイベントを送信するためのサービスクラスです。
 *
 * <p>
 * このクラスは、指定したイベントバスに対して任意の JSON 形式のイベントを送信する機能を提供します。
 * AWS SDK for Java v2 を利用して実装されています。
 * </p>
 *
 * <h2>使用例</h2>
 * <pre>{@code
 * EventBridgeClient client = EventBridgeClient.builder().build();
 * EventBridgeService service = new EventBridgeService(client, "my-custom-bus");
 *
 * String detailJson = "{ \"notify\": \"END_OF_WORK\" }";
 * service.sendEvent("MyEventBusName", "MySource", "MyDetailType", detailJson);
 * }</pre>
 *
 * <p>
 * 本クラスを利用するには、AWS の認証情報とリージョン設定が必要です。
 * 環境変数、システムプロパティ、または AWS プロファイルのいずれかで設定してください。
 * </p>
 *
 * @see software.amazon.awssdk.services.eventbridge.EventBridgeClient
 */
public interface EventBridgeService {

    /**
     * カスタムイベントを送信します.
     *
     * @param eventBusName イベントバス名
     * @param source イベントの発信元
     * @param detailType イベントの種類を表すラベル
     * @param detailJson JSON形式のイベント詳細データ
     */
    void sendCustomEvent(String eventBusName, String source, String detailType, String detailJson);
}
