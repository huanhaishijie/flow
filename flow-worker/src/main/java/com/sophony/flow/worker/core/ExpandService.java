package com.sophony.flow.worker.core;

import com.sophony.flow.absEo.BaseMappingEO;
import com.sophony.flow.commons.StringUtils;
import com.sophony.flow.worker.base.DataService;
import com.sophony.flow.worker.base.FlowUserInfo;
import com.sophony.flow.worker.modle.User;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * ExpandService
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 13:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExpandService implements DataService {


    @Resource
    JdbcTemplate jdbcTemplate;


    private FlowUserInfo flowUserInfo;


    public void setFlowUserInfo(FlowUserInfo flowUserInfo){
        this.flowUserInfo = flowUserInfo;
    }

    @Override
    @SneakyThrows
    public String insert(BaseMappingEO eo) {
        User currentUser = flowUserInfo.getCurrentUser();

        Date date = new Date();
        eo.setUpdateTime(date);
        eo.setCreateTime(date);
        if(Objects.nonNull(currentUser)){
            eo.setCreateUser(currentUser.getUserName());
            eo.setUpdateUser(currentUser.getUserName());
        }


        String insertSql = eo.getInsertSql();
        jdbcTemplate.update(insertSql , eo.getArgs());
        return eo.getId();
    }

    @Override
    @SneakyThrows
    public void updateById(BaseMappingEO eo) {
        User currentUser = flowUserInfo.getCurrentUser();
        if(Objects.nonNull(currentUser)){
            eo.setUpdateUser(currentUser.getUserName());
            eo.setUpdateTime(new Date());
        }
        String sql = eo.getUpdateSql() + " where id = ?";
        List<Object> objects = new ArrayList<Object>(){{
            addAll(Arrays.asList(eo.getArgs()));
        }};
        objects.add(eo.getId());
        jdbcTemplate.update(sql, objects.toArray());
    }

    @Override
    @SneakyThrows
    public void deleteById(BaseMappingEO eo) {
        if(!StringUtils.isNotBlank(eo.getId())){
            return;
        }
        String sql = "update "+ eo.getTableName() + " set is_deleted = ? where id = ? ";
        User currentUser = flowUserInfo.getCurrentUser();
        if(Objects.nonNull(currentUser)){
            eo.setUpdateUser(currentUser.getUserName());
            eo.setUpdateTime(new Date());
            sql = " update " + eo.getTableName() + " set  update_user = ? , update_time = ? , is_deleted = ? where id = ?";
            jdbcTemplate.update(sql, eo.getUpdateUser(), eo.getUpdateTime(), 1, eo.getId());
            return;
        }
        jdbcTemplate.update(sql, 1, eo.getId());
    }


    @Override
    public User getCurrentUser() {
        return flowUserInfo.getCurrentUser();
    }

    @Override
    @SneakyThrows
    public List selectByIds(String nextTaskIds, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO)aClass.newInstance();
        String querySql = baseMappingEO.getQuerySql();
        querySql+= " where is_deleted = 0 and id in " + conditionByIn(nextTaskIds, String.class);
        return list(querySql, aClass, nextTaskIds.split(","));
    }

    @Override
    @SneakyThrows
    public List list(String sql, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        List res = new ArrayList();
        try {
            res = jdbcTemplate.query(sql, baseMappingEO);
        }catch (Exception e){
            res.add(jdbcTemplate.queryForObject(sql, baseMappingEO));
        }
        return res;
    }

    @Override
    @SneakyThrows
    public List list(String sql, Class aClass, Object[] args) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        List res = new ArrayList();
        try {
            res = jdbcTemplate.query(sql, baseMappingEO, args);
        }catch (Exception e){
            res.add(jdbcTemplate.queryForObject(sql, baseMappingEO, args));
        }
        return res;
    }

    @Override
    @SneakyThrows
    public BaseMappingEO getById(String id, Class aClass) {
        BaseMappingEO baseMappingEO = (BaseMappingEO) aClass.newInstance();
        String sql = baseMappingEO.getQuerySql() +" where id = ? and is_deleted = 0 limit 1 ";
        return (BaseMappingEO) jdbcTemplate.queryForObject(sql, baseMappingEO, id);
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


    public User getCurrent(){
        User user = new User();
        if(flowUserInfo == null){
            user.setUserId("None");
            user.setUserName("None");
        }else {
             user = flowUserInfo.getCurrentUser();
        }
        return user;
    }


}
