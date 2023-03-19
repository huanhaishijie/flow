package com.sophony.flow.worker.persistence;

import com.sophony.flow.commons.BusParam;

/**
 * TaskPersistenceService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 14:15
 */
public class CachePersistenceService {

    private CacheDAO cacheDAO;

    public void init() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.initDatasource();
        cacheDAO = new CacheDAOImpl(connectionFactory);
        cacheDAO.initTable();
        BusParam.getInstance().getMap().put("CacheService", cacheDAO);
    }

    public CacheDAO getCacheDAO() {
        return cacheDAO;
    }
}
