package net.hollage.libs.aws.eventbridge.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SdkUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Map<String, Object> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("JSON変換に失敗しました", e);
        }
    }
}
