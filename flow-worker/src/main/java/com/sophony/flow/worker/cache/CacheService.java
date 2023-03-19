package com.sophony.flow.worker.cache;

/**
 * CacheService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 16:21
 */
public interface CacheService {

    String getStr(String key);

    <T> T getObj(String key, Class<T> clazz);

    void setObj(String key, Object val);

    void setObj(String key, Object val, Long expireSecond);

    void hset(String key, String hKey, Object val);

    void hset(String key, String hKey, Object val, Long expireSecond);

    <T> T hget(String key, String hKey, Class<T> clazz);

    void hdel(String key, String hKey);
}
