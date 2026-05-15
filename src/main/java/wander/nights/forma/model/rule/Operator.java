package wander.nights.forma.model.rule;

import lombok.Getter;

@Getter
public enum Operator {
    EQ("等于"),
    NE("不等于"),
    CONTAINS("包含"),
    NOT_CONTAINS("不包含"),
    GT("大于"),
    LT("小于"),
    GE("大于等于"),
    LE("小于等于"),
    FILLED("已填写"),
    EMPTY("未填写");

    private final String label;

    Operator(String label) {
        this.label = label;
    }
}
