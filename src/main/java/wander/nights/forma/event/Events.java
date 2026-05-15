package wander.nights.forma.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class Events {
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
