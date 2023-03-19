package com.sophony.flow.worker.persistence;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CacheDAOImpl
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 14:18
 */
public class CacheDAOImpl implements CacheDAO{

    private final ConnectionFactory connectionFactory;

    public CacheDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public void initTable() throws Exception{
        String delTableSQL = "drop table if exists cache_info";
        String createTableSQL = "CREATE TABLE cache_info ( `key` VARCHAR (2000),`group` VARCHAR (2000),value VARCHAR (50000),deathLine BIGINT, `type` VARCHAR (2000), CONSTRAINT pkey UNIQUE (`group`))";

        try (Connection conn = connectionFactory.getConnection(); Statement stat = conn.createStatement()) {
            stat.execute(delTableSQL);
            stat.execute(createTableSQL);
        }
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new Task(this.connectionFactory), 0, 3, TimeUnit.SECONDS);
    }

    @Override
    public boolean save(CacheDO cacheDO) throws SQLException {
        if(StringUtils.equals("Hash", cacheDO.getGroup())){

        }else {
            if(StringUtils.isNotEmpty(getStr(cacheDO))){
                cacheDO.setType("String");
                cacheDO.setGroup(null);
                delete(cacheDO);
            }

        }


        String insertSQL = "insert into cache_info(`key`, `group`, `value`, deathLine, `type`) values (?,?,?,?,?)";
        try (Connection conn = connectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            fillInsertPreparedStatement(cacheDO, ps);
            return ps.executeUpdate() == 1;
        }
    }



    @Override
    public boolean batchSave(Collection<CacheDO> cacheList) throws SQLException {
        String insertSQL = "insert into cache_info(`key`, `group`, `value`, deathLine, `type`) values (?,?,?,?,?)";
        boolean originAutoCommitFlag ;
        try (Connection conn = connectionFactory.getConnection()) {
            originAutoCommitFlag = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try ( PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                for (CacheDO cacheDO : cacheList) {
                    fillInsertPreparedStatement(cacheDO, ps);
                    ps.addBatch();
                }
                ps.executeBatch();
                return true;
            } catch (Throwable e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originAutoCommitFlag);
            }
        }
    }



    /**
     * 删除缓存
     * @param cacheDO
     * @return
     * @throws SQLException
     */
    @Override
    public boolean delete(CacheDO cacheDO) throws SQLException {
        String sql = " delete from cache_info where 1=1 ";
        if(Objects.isNull(cacheDO)){
            return false;
        }
        if(StringUtils.isEmpty(cacheDO.getKey())){
            return false;
        }
        if(StringUtils.isNotEmpty(cacheDO.getKey())){
            sql += " and `key` = '"+cacheDO.getKey()+"'";
        }
        if(StringUtils.isNotEmpty(cacheDO.getGroup())){
            sql += " and `group` = '"+cacheDO.getGroup()+"'";
        }
        try (Connection conn = connectionFactory.getConnection(); Statement stat = conn.createStatement()) {
            stat.executeUpdate(sql);
            return true;
        }
    }


    /**
     * 获取String类型
     * @param cacheDO
     * @return
     * @throws SQLException
     */
    @Override
    public String getStr(CacheDO cacheDO) throws SQLException {
        ResultSet rs = null;
        String sql = "select * from cache_info where `key` = ? and `type` = 'String' limit 1 ";
        List<CacheDO> result = Lists.newLinkedList();
        try (Connection conn = connectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cacheDO.getKey());
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(convert(rs));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (Exception ignore) {
                }
            }
        }
        if(result == null || result.size() == 0){
            return null;
        }
        return result.get(0).getValue();
    }

    @Override
    public String hget(CacheDO cacheDO) throws SQLException {
        if(Objects.isNull(cacheDO) || StringUtils.isEmpty(cacheDO.getKey()) || StringUtils.isEmpty(cacheDO.getGroup())){
            return null;
        }
        ResultSet rs = null;
        String sql = "select * from cache_info where `key` = ? and `group` = ? and `type` = 'Hash' limit 1";
        List<CacheDO> result = Lists.newLinkedList();
        try (Connection conn = connectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cacheDO.getKey());
            ps.setString(2, cacheDO.getGroup());
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(convert(rs));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (Exception ignore) {
                }
            }
        }
        if(result == null || result.size() == 0){
            return null;
        }
        return result.get(0).getValue();
    }


    private void fillInsertPreparedStatement(CacheDO cacheDO, PreparedStatement ps) throws SQLException {
        ps.setString(1, cacheDO.getKey());
        ps.setString(2, cacheDO.getGroup());
        ps.setString(3, cacheDO.getValue());
        ps.setLong(4, cacheDO.getDeathLine());
        ps.setString(5, cacheDO.getType());
    }

    private static CacheDO convert(ResultSet rs) throws SQLException {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setKey(rs.getString("key"));
        cacheDO.setValue(rs.getString("value"));
        cacheDO.setGroup(rs.getString("group"));
        cacheDO.setType(rs.getString("type"));
        return cacheDO;
    }



    class Task implements Runnable{
        ConnectionFactory connectionFactory;
        public Task(ConnectionFactory connectionFactory){
            this.connectionFactory = connectionFactory;
        }

        //清除失效缓存
        @Override
        public void run() {
            long currentSeconds = System.currentTimeMillis() / 1000;
            String sql = "delete from cache_info where deathLine > 0 and deathLine < " + currentSeconds;
            try (Connection conn = connectionFactory.getConnection(); Statement stat = conn.createStatement()) {
                stat.executeUpdate(sql);
            } catch (Exception e) {
                System.out.println("定时任务失败——————————————————————————————————————————————————————————");
            }
        }
    }
}
