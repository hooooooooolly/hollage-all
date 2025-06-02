package net.hollage.libs.aws.eventbridge.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/** SDK用ユーティリティクラス. */
public class SdkUtils {

    /** JSONマッパー. */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * MapをJSON文字列に変換する.
     *
     * @param map Mapオブジェクト
     * @return JSON文字列
     */
    public static String toJson(Map<String, Object> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("JSON変換に失敗しました", e);
        }
    }
}
