package com.sophony.flow.worker.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

/**
 * SqlInitExcued
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/17 16:58
 */
@Slf4j
public abstract class SqlInitExecute {

    protected JdbcTemplate jdbcTemplate;
    public abstract void execute();


    public void setJdbcTemplate(JdbcTemplate template){
        this.jdbcTemplate = template;
    }


}
