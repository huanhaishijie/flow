package com.sophony.flow.worker.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * RedisCacheService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 16:27
 */

@Service
@Slf4j
public class FlowRedisCacheService implements CacheService{

    @Resource
    StringRedisTemplate  stringRedisTemplate;


    @Override
    public String getStr(String key) {
        return String.valueOf(stringRedisTemplate.opsForValue().get(key));
    }


    @Override
    public <T> T getObj(String key, Class<T> clazz) {
        String res = String.valueOf(stringRedisTemplate.opsForValue().get(key));
        if(StringUtils.isEmpty(res)){
            return null;
        }
        try {
            return JSONObject.parseObject(res, clazz);
        }catch (Exception e){
            log.info("当前key:{}, 获取内容:{}, json转换实体失败", key, res);
        }
        return null;
    }

    @Override
    public void setObj(String key, Object val) {
        String v = JSON.toJSONString(val);
        stringRedisTemplate.opsForValue().set(key, v);
    }

    @Override
    public void setObj(String key, Object val, Long expireSecond) {
        String v = JSON.toJSONString(val);
        stringRedisTemplate.opsForValue().set(key, v);
        stringRedisTemplate.expire(key, expireSecond, TimeUnit.SECONDS);
    }

    @Override
    public void hset(String key, String hKey, Object val) {
        String v = JSON.toJSONString(val);
        stringRedisTemplate.opsForHash().put(key, hKey, v);
    }

    @Override
    public void hset(String key, String hKey, Object val, Long expireSecond) {
        String expireKey = key +":"+ hKey;
        String v = JSON.toJSONString(val);
        stringRedisTemplate.opsForHash().put(key, hKey, v);
        stringRedisTemplate.expire(key, 2L, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(expireKey, expireKey);
        stringRedisTemplate.expire(expireKey, expireSecond, TimeUnit.SECONDS);
    }


    @Override
    public <T> T hget(String key, String hKey, Class<T> clazz) {
        String expireKey = key +":"+ hKey;
        Boolean f = stringRedisTemplate.hasKey(expireKey);
        if(!f){
            stringRedisTemplate.opsForHash().delete(key, hKey);
            return null;
        }
        String res = String.valueOf(stringRedisTemplate.opsForHash().get(key, hKey));
        T t = JSONObject.parseObject(res, clazz);
        return t;
    }


    @Override
    public void hdel(String key, String hKey) {
        String expireKey = key +":"+ hKey;
        stringRedisTemplate.delete(expireKey);
        stringRedisTemplate.opsForHash().delete(key, hKey);
    }


}
