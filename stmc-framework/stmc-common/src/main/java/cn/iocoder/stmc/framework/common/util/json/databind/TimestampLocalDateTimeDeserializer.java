package cn.iocoder.stmc.framework.common.util.json.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 基于时间戳的 LocalDateTime 反序列化器
 * 同时支持时间戳和字符串格式
 *
 * @author 老五
 */
public class TimestampLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public static final TimestampLocalDateTimeDeserializer INSTANCE = new TimestampLocalDateTimeDeserializer();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 如果是字符串，尝试按日期格式解析
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            String text = p.getText().trim();
            if (text.isEmpty()) {
                return null;
            }
            try {
                // 尝试完整格式 yyyy-MM-dd HH:mm:ss
                return LocalDateTime.parse(text, FORMATTER);
            } catch (DateTimeParseException e) {
                try {
                    // 尝试日期格式 yyyy-MM-dd
                    return LocalDateTime.parse(text + " 00:00:00", FORMATTER);
                } catch (DateTimeParseException e2) {
                    // 如果都失败，返回null
                    return null;
                }
            }
        }
        // 将 Long 时间戳，转换为 LocalDateTime 对象
        long timestamp = p.getValueAsLong();
        if (timestamp == 0) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

}
