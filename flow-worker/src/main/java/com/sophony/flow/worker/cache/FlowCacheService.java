package com.sophony.flow.worker.cache;

import com.sophony.flow.commons.BusParam;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.persistence.CacheDAO;
import com.sophony.flow.worker.persistence.CachePersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FlowCacheSerivce
 *
 * @author yzm
 * @version 1.0
 * @description 缓存服务
 * @date 2023/3/19 16:17
 */
@Slf4j
public class FlowCacheService {

    /**
     * 全局是否开启缓存
     */
    private static   boolean isOpenCache = false;


    private final AtomicBoolean f = new AtomicBoolean(true);
    CacheService cacheService;

    public FlowCacheService(){
    }

    public void init() throws Exception{
        if(!f.get()){
            return;
        }
        boolean openCache = (boolean) BusParam.getInstance().getMap().get("openCache");
        if(!openCache){
            isOpenCache = false;
            return;
        }
        isOpenCache = true;
        String cacheType = (String) BusParam.getInstance().getMap().get("cacheType");

        switch (cacheType){
            case "H2":
                CachePersistenceService cachePersistenceService = new CachePersistenceService();
                cachePersistenceService.init();
                CacheDAO cacheDAO = cachePersistenceService.getCacheDAO();
                cacheService = new H2CacheService(cacheDAO);
                break;
            case "redis":
                cacheService = FlowBeanFactory.getInstance().getBean(FlowRedisCacheService.class);
                StringRedisTemplate stringRedisTemplate = FlowBeanFactory.getInstance().getBean(StringRedisTemplate.class);
                if(Objects.isNull(stringRedisTemplate)){
                    log.info("当前环境需要配置redis");
                    FlowBeanFactory.getInstance().shutdown();
                }
                ((FlowRedisCacheService)cacheService).setStringRedisTemplate(stringRedisTemplate);
                break;
        }
        f.set(false);
    }


    public String getStr(String key){
        if(!isOpenCache){
            return null;
        }
        return cacheService.getStr(key);
    }

    public <T> T getObj(String key, Class<T> clazz){
        if(!isOpenCache){
            return null;
        }
        return cacheService.getObj(key, clazz);
    }

    public void setObj(String key, Object val){
        if(!isOpenCache){
            return;
        }
        cacheService.setObj(key, val);
    }

    public void setObj(String key, Object val, Long expireSecond){
        if(!isOpenCache){
            return;
        }
        cacheService.setObj(key, val, expireSecond);
    }

    public void hset(String key, String hKey, Object val){
        if(!isOpenCache){
            return;
        }
        cacheService.hset(key, hKey, val);
    }

    public void hset(String key, String hKey, Object val, Long expireSecond){
        if(!isOpenCache){
            return;
        }
        cacheService.hset(key, hKey, val, expireSecond);
    };

    public <T> T hget(String key, String hKey, Class<T> clazz){
        if(!isOpenCache){
            return null;
        }
        return cacheService.hget(key, hKey, clazz);
    }

    public void hdel(String key, String hKey) {
        if(!isOpenCache){
            return ;
        }
        cacheService.hdel(key, hKey);
    }
}
