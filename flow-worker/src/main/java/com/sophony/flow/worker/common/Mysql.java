package com.sophony.flow.worker.common;

import com.sophony.flow.commons.MysqlInitSql;
import com.sophony.flow.commons.PostgresqlInitSql;

/**
 * Mysql
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/18 12:07
 */
public class Mysql extends SqlInitExecute{


    @Override
    public void execute() {
        boolean valid = valid();
        if(!valid){
            return;
        }
        MysqlInitSql.sqls.forEach(jdbcTemplate::execute);
    }

    private boolean valid(){
        boolean f = true;
        for (String sql : MysqlInitSql.validSql) {
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class);
            if(num > 0){
                f = false;
                break;
            }
        }
        return f;
    }







}
