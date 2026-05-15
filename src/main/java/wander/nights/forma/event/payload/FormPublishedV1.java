package wander.nights.forma.event.payload;

import wander.nights.forma.event.DomainEvent;

public class FormPublishedV1 implements DomainEvent {
    @Override
    public String eventType() {
        return "form.published";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
