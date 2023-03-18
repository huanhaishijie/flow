package com.sophony.flow.absEo;

import com.alibaba.fastjson.JSON;
import com.sophony.flow.commons.BusParam;
import com.sophony.flow.utils.ParseSql;
import com.sophony.flow.commons.SnowFlakeWorker;
import com.sophony.flow.utils.ParseSqlByMySql;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * BaseMappingEO
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/7 13:39
 */
public abstract class BaseMappingEO<R extends BaseMappingEO> implements RowMapper<R> {
    ParseSql parseSql;
    private String id;


    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 拓展数据
     */
    private String expansion;



    /**
     * 创建人
     */

    private String createUser;

    /**
     * 更新人
     */

    private String updateUser;

    /**
     * 是否删除 0否， 1是
     */

    private Integer isDeleted;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }

    public Map<String, Object> getExpansionData() {
        if(Objects.nonNull(this.getExpansion())){
            try {
                return JSON.parseObject(this.getExpansion());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    public void setExpansionData(Map<String, Object> expansionData) {
        try {
            this.expansion = JSON.toJSONString(expansionData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public abstract String getTableName();


    private   R mappingRow(ResultSet rs, int rowNum) throws SQLException, InvocationTargetException, IllegalAccessException {
        return parseSql.mapRow(rs, rowNum);
    }


    public R mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return mappingRow(rs, rowNum);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }





    public String getQuerySql(){
        return parseSql.getQuerySql();
    }

    public String getUpdateSql() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return parseSql.getUpdateSql();
    }

    public String getInsertSql() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return parseSql.getInsertSql();
    }

    public void generateId() {
        this.id = SnowFlakeWorker.idGenerate();
    }


    public Object[] getArgs(){
        return parseSql.getArgs().toArray();

    }

    public void copyProperties(Object resource){
        if(Objects.isNull(resource)){
            throw new  RuntimeException("源数据为空,不能拷贝数据");
        }
        BeanUtils.copyProperties(resource, this);
    }

    public <E> E copyProperties(Class<E> target){
        if(Objects.isNull(target)){
            throw new  RuntimeException("目标类型为空,不能拷贝数据");
        }
        E r = null;
        try {
            r = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(this, r);
        return r;
    }


    /**
     * 支持mysql和 postgrepsql 两种数据解析
     */
    public BaseMappingEO(){
        String sqlType = BusParam.getInstance().getSqlType();
        switch (sqlType){
            case "mysql":
                this.parseSql = new ParseSqlByMySql(this);
                break;
            case "postgresql":
                this.parseSql = new ParseSql(this);
                break;
        }
    }

}
