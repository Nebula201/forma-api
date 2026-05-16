package wander.nights.forma.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.UUID;

public class Events {
    private static final ObjectMapper MAPPER = objectMapper();

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册 JSR310 模块
        mapper.registerModule(new JavaTimeModule());
        // 禁用将日期写为时间戳，使用 ISO-8601 格式
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static String serialize(FormaEvent<?> event) {
        try {
            return MAPPER.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FormaEvent<?> deserialize(String json) {
        try {
            JsonNode root = MAPPER.readTree(json);
            String eventType = root.get("eventType").asText();
            int version = root.get("eventVersion").asInt();

            Class<? extends DomainEvent> clazz = EventRegistry.get(eventType, version);
            JsonNode payloadNode = root.get("payload");

            DomainEvent payload = MAPPER.treeToValue(payloadNode, clazz);
            FormaEvent<DomainEvent> envelope = new FormaEvent<>();
            envelope.setEventId(UUID.fromString(root.get("eventId").asText()));
            envelope.setEventType(eventType);
            envelope.setEventVersion(version);
            envelope.setPayload(payload);
            return envelope;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
