package wander.nights.forma.submission.infrastructure.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SQL 片段及参数封装
 *
 * @param sql    SQL 条件片段，使用 ? 占位符
 * @param params 对应占位符的参数值
 */
public record SqlFragment(String sql, List<Object> params) {

    public static SqlFragment empty() {
        return new SqlFragment("", Collections.emptyList());
    }

    // 合并多个 SqlFragment（用 AND 连接）
    public static SqlFragment and(List<SqlFragment> fragments) {
        if (fragments == null || fragments.isEmpty()) return empty();
        List<String> sqlParts = new ArrayList<>();
        List<Object> allParams = new ArrayList<>();
        for (SqlFragment frag : fragments) {
            if (frag == null || frag.sql().isEmpty()) continue;
            sqlParts.add(frag.sql());
            allParams.addAll(frag.params());
        }
        if (sqlParts.isEmpty()) return empty();
        return new SqlFragment(String.join(" AND ", sqlParts), allParams);
    }

    // 合并多个 SqlFragment（用 OR 连接）
    public static SqlFragment or(List<SqlFragment> fragments) {
        if (fragments == null || fragments.isEmpty()) return empty();
        List<String> sqlParts = new ArrayList<>();
        List<Object> allParams = new ArrayList<>();
        for (SqlFragment frag : fragments) {
            if (frag == null || frag.sql().isEmpty()) continue;
            sqlParts.add(frag.sql());
            allParams.addAll(frag.params());
        }
        if (sqlParts.isEmpty()) return empty();
        return new SqlFragment("(" + String.join(" OR ", sqlParts) + ")", allParams);
    }
}