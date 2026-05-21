package wander.nights.forma.shared.condition;

import lombok.Getter;

@Getter
public enum Operator {
    // 文本比较
    EQ("等于"),
    NE("不等于"),
    CONTAINS("包含"),
    NOT_CONTAINS("不包含"),
    START_WITH("以开头"),
    END_WITH("以结尾"),

    // 集合比较
    IN("在列表"),
    NOT_IN("不在列表"),

    // 数值比较
    GT("大于"),
    LT("小于"),
    GE("大于等于"),
    LE("小于等于"),

    // 空值比较
    FILLED("已填写"),
    EMPTY("未填写");

    private final String label;

    Operator(String label) {
        this.label = label;
    }
}