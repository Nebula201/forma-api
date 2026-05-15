package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评分字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RatingField extends FieldDefinition {

    private Integer min = 1;
    private Integer max = 5;
    private String icon = "star";  // star, heart, smile
    private String lowLabel;       // 低分描述
    private String highLabel;      // 高分描述
}