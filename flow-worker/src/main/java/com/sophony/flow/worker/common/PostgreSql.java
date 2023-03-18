package com.sophony.flow.worker.common;

import com.sophony.flow.commons.PostgresqlInitSql;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Objects;

/**
 * PostgreSql
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/18 12:08
 */
public class PostgreSql extends SqlInitExecute{

    @Override
    public void execute() {
        boolean valid = valid();
        if(!valid){
            return;
        }
        jdbcTemplate.execute(PostgresqlInitSql.sql);
    }

    boolean valid(){
        boolean f = true;
        String schema = getSchema();
        if(Objects.isNull(schema)){
            return f;
        }
        for (String sql : PostgresqlInitSql.validSql) {
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, schema);
            if(num > 0){
                f = false;
                break;
            }
        }
        return f;
    }


    String getSchema() {
        try {
            return jdbcTemplate.getDataSource().getConnection().getSchema();
        }catch (Exception e){
            return null;
        }

    }




}
