package com.sophony.flow.worker.persistence;

import java.sql.SQLException;
import java.util.Collection;

/**
 * CacheDAO
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 14:17
 */
public interface CacheDAO {
    void initTable() throws Exception;

    boolean save(CacheDO cacheDO) throws SQLException;

    boolean batchSave(Collection<CacheDO> cacheList) throws SQLException;


    boolean delete(CacheDO cacheDO) throws SQLException;


    /**
     * 获取String 内容
     * @param cacheDO
     * @return
     * @throws SQLException
     */
    String getStr(CacheDO cacheDO) throws SQLException;

    /**
     * 获取hash内容
     * @param cacheDO
     * @return
     * @throws SQLException
     */
    String hget(CacheDO cacheDO) throws SQLException;



}
