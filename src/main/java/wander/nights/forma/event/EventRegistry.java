package wander.nights.forma.event;

import wander.nights.forma.event.payload.FormCreatedV1;

import java.util.HashMap;
import java.util.Map;

class EventRegistry {
    private static final Map<String, Class<? extends DomainEvent>> EVENTS = new HashMap<>();

    static {
        register("form.created:v1", FormCreatedV1.class);
    }

    public static void register(String key, Class<? extends DomainEvent> clazz) {
        EVENTS.put(key, clazz);
    }

    public static Class<? extends DomainEvent> get(String eventType, int version) {
        return EVENTS.get(eventType + ":v" + version);
    }
}
