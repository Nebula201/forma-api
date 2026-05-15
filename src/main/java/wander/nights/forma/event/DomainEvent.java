package wander.nights.forma.event;

public interface DomainEvent {
    String eventType();

    int eventVersion();
}
