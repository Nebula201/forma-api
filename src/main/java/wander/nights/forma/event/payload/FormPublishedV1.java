package wander.nights.forma.event.payload;

import lombok.Data;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.entity.FormVersion;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import java.time.Instant;

@Data
public class FormPublishedV1 implements DomainEvent {
    private FormId formId;
    private Integer formVersion;
    private String formTitle;
    private String url;
    private UserId publishedBy;
    private Instant publishedAt;

    public FormPublishedV1(Form form, FormVersion formVersion) {
        this.formId = form.getFormId();
        this.formTitle = form.getTitle();
        this.formVersion = formVersion.getFormVersion();
        this.publishedBy = formVersion.getPublishedBy();
        this.publishedAt = formVersion.getPublishedAt();
    }

    @Override
    public String eventType() {
        return "form.published";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
