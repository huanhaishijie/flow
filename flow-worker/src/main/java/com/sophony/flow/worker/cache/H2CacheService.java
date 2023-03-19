package com.sophony.flow.worker.cache;

import com.alibaba.fastjson.JSONObject;
import com.sophony.flow.worker.persistence.CacheDAO;
import com.sophony.flow.worker.persistence.CacheDO;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

/**
 * H2CacheService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 16:34
 */
public class H2CacheService implements CacheService{
    CacheDAO cacheDAO;

    private final static String STR = "String";
    private final static String HASH = "Hash";



    public H2CacheService(CacheDAO cacheDAO) {
        this.cacheDAO = cacheDAO;
    }

    @Override
    public String getStr(String key) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(STR);
        cacheDO.setKey(key);
        try {
            return cacheDAO.getStr(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getObj(String key, Class<T> clazz) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(STR);
        cacheDO.setKey(key);
        try {
            String str = cacheDAO.getStr(cacheDO);
            if(StringUtils.isEmpty(str)){
                return null;
            }
            return JSONObject.parseObject(str, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setObj(String key, Object val) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(STR);
        cacheDO.setKey(key);
        cacheDO.setValue(JSONObject.toJSONString(val));
        try {
            cacheDAO.save(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setObj(String key, Object val, Long expireSecond) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(STR);
        cacheDO.setKey(key);
        cacheDO.setValue(JSONObject.toJSONString(val));
        if(expireSecond == null || expireSecond < 0){
            expireSecond = 5L;
        }
        long time = System.currentTimeMillis() / 1000 + expireSecond;
        cacheDO.setDeathLine(time);
        try {
            cacheDAO.save(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void hset(String key, String hKey, Object val) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(HASH);
        cacheDO.setGroup(hKey);
        cacheDO.setKey(key);
        cacheDO.setValue(JSONObject.toJSONString(val));
        try {
            cacheDAO.save(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void hset(String key, String hKey, Object val, Long expireSecond) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(HASH);
        cacheDO.setGroup(hKey);
        cacheDO.setKey(key);
        cacheDO.setValue(JSONObject.toJSONString(val));
        if(expireSecond == null || expireSecond < 0){
            expireSecond = 5L;
        }
        long time = System.currentTimeMillis() / 1000 + expireSecond;
        cacheDO.setDeathLine(time);
        try {
            cacheDAO.save(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public <T> T hget(String key, String hKey, Class<T> clazz) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(HASH);
        cacheDO.setGroup(hKey);
        cacheDO.setKey(key);
        try {
            String res = cacheDAO.hget(cacheDO);
            if(StringUtils.isEmpty(res)){
                return null;
            }
            return  JSONObject.parseObject(res, clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void hdel(String key, String hKey) {
        CacheDO cacheDO = new CacheDO();
        cacheDO.setType(HASH);
        cacheDO.setGroup(hKey);
        cacheDO.setKey(key);
        try {
            cacheDAO.delete(cacheDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
