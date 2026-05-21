package wander.nights.forma.model.rule;

import lombok.Data;
import lombok.Getter;
import wander.nights.forma.shared.identifier.FieldCode;

@Data
public class Condition {
    /**
     * 表单项Id
     */
    private FieldCode field;
    /**
     * 操作符
     */
    private Operator operator;
    /**
     * 比较值（选项ID或数值）
     */
    private String value;

    @Getter
    public enum Type {
        ALL("全部满足"),

        ANY("任一满足");

        private final String desc;

        Type(String desc) {
            this.desc = desc;
        }
    }
}
