package com.sophony.flow.worker.core;

import com.sophony.flow.absEo.BaseMappingEO;
import com.sophony.flow.commons.StringUtils;
import com.sophony.flow.worker.base.DataService;
import com.sophony.flow.worker.modle.User;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * DefauteService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 10:23
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class DefaultService implements DataService {

    @Resource
    JdbcTemplate jdbcTemplate;


    @SneakyThrows
    @Override
    public String insert(BaseMappingEO eo) {
        Date date = new Date();
        eo.setUpdateTime(date);
        eo.setCreateTime(date);
        jdbcTemplate.update(eo.getInsertSql(), new ArgumentPreparedStatementSetter(eo.getArgs()));
        return eo.getId();
    }

    @SneakyThrows
    @Override
    public void updateById(BaseMappingEO eo) {
        eo.setUpdateTime(new Date());
        String updateSql = eo.getUpdateSql();
        updateSql += " where id = '"+ eo.getId()+"'";
        jdbcTemplate.update(updateSql, new ArgumentPreparedStatementSetter(eo.getArgs()));
    }

    @Override
    public void deleteById(BaseMappingEO eo) {
        if(StringUtils.isNotBlank(eo.getId())){
            String sql = " delete from "+ eo.getTableName() +" where id = '"+eo.getId()+"'";
            jdbcTemplate.update(sql);
        }

    }

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    @SneakyThrows
    public BaseMappingEO selectOne(String sql, Class aClass, Object[] args) {
        String countSql = "select count(*) from ("+ sql+") as a";
        Integer count = 0;
        if(args.length == 0){
            count =jdbcTemplate.queryForObject(countSql, Integer.class);
        }else {
            count = jdbcTemplate.queryForObject(countSql, Integer.class, args);
        }
        if(count > 1 || count == 0){
            return null;
        }

        return (BaseMappingEO)jdbcTemplate.queryForObject(sql, (RowMapper<? extends Object>) aClass.newInstance(), args);
    }

    @Override
    @SneakyThrows
    public List list(String sql, Class aClass, Object[] args) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        List res = jdbcTemplate.query(sql, baseMappingEO, args);
        return res;
    }

    @Override
    @SneakyThrows
    public List list(String sql, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        List list = jdbcTemplate.query(sql, baseMappingEO);
        return list;
    }

    @Override
    @SneakyThrows
    public BaseMappingEO getById(String id, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        String sql = "select * from "+ baseMappingEO.getTableName() +" where id = ? and is_deleted = 0 limit 1 ";
        return (BaseMappingEO) jdbcTemplate.queryForObject(sql, baseMappingEO, id);
    }


    @Override
    @SneakyThrows
    public List selectByIds(String nextTaskIds, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO)aClass.newInstance();
        String querySql = baseMappingEO.getQuerySql();
        querySql+= " where is_deleted = 0 and id in " + conditionByIn(nextTaskIds, String.class);
        return list(querySql, aClass);
    }



}
