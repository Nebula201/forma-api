package wander.nights.forma.event.payload;

import wander.nights.forma.event.DomainEvent;

public class FormCreatedV1 implements DomainEvent {
    @Override
    public String eventType() {
        return "form.created";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
