package wander.nights.forma.form.command.dto;

import lombok.Data;
import wander.nights.forma.shared.valueobject.UserId;

public class FormCollaboratorRequests {

    @Data
    public static class Query {
        private String roleCode;
        private String userName;
    }


    @Data
    public static class AddRequest {
        private String roleCode;
        private UserId userId;
    }
}
