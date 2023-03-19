package com.sophony.flow.worker.persistence;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * SimpleCacheQuery
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 15:08
 */
@Data
public class SimpleCacheQuery {


    private static final String LINK = " and ";

    String key;

    String group;

    Long deathLine;

    // 自定义的查询条件（where 后面的语句），如 crated_time > 10086 and status = 3
    private String queryCondition;
    // 自定义的查询条件，如 GROUP BY status
    private String otherCondition;

    // 查询内容，默认为 *
    private String queryContent = " * ";

    private Integer limit;

    public String getQueryCondition() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(key)) {
            sb.append("`key` = '").append(key).append("'").append(LINK);
        }

        if (!StringUtils.isEmpty(group)) {
            sb.append("`group` = '").append(group).append("'").append(LINK);
        }


        if (!StringUtils.isEmpty(queryCondition)) {
            sb.append(queryCondition).append(LINK);
        }

        String substring = sb.substring(0, sb.length() - LINK.length());

        if (!StringUtils.isEmpty(otherCondition)) {
            substring += otherCondition;
        }

        if (limit != null) {
            substring = substring + " limit " + limit;
        }

        return substring;
    }
}
