package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文件上传字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileField extends FieldDefinition {

    private List<String> accept = List.of("image/*", ".pdf", ".doc");
    private Integer maxSize = 10;  // MB
    private Integer maxCount = 1;
}