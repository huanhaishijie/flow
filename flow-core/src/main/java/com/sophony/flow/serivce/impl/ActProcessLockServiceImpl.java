package com.sophony.flow.serivce.impl;

import com.sophony.flow.commons.constant.ProcessLockEnum;
import com.sophony.flow.mapping.ActProcessLock;
import com.sophony.flow.serivce.IActProcessLockService;
import com.sophony.flow.worker.common.BaseService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * ActProcessLockServiceImpl
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/9 23:22
 */
@Service
public class ActProcessLockServiceImpl extends BaseService implements IActProcessLockService {

    @Resource
    JdbcTemplate jdbcTemplate;


    /**
     * 获取锁
     *
     * @param processId
     * @param second
     */
    @Override
    public ActProcessLock getLock(String processId, Long second) {
        ActProcessLock actProcessLock = new ActProcessLock();
        LocalDateTime validTime = LocalDateTime.now().plusSeconds(second);
        String sql = actProcessLock.getQuerySql() + " where is_deleted = 0 and valid_time > now() and status = 'lock' and process_id = ? order by create_time desc limit 1 ";
        actProcessLock = super.selectOne(sql, ActProcessLock.class, new Object[]{processId});
        return actProcessLock;


    }

    @Override
    public String lock(String processId, Long second) {
        LocalDateTime validTime = LocalDateTime.now().plusSeconds(second);
        ActProcessLock actProcessLock = new ActProcessLock();
        actProcessLock.setProcessId(processId);
        actProcessLock.setStatus(ProcessLockEnum.LOCK.getName());
        actProcessLock.setValidTime(validTime);
        return super.insert(actProcessLock);
    }


    @Override
    public void joinTime(ActProcessLock lock, Long joinTime) {
        LocalDateTime localDateTime = lock.getValidTime().plusSeconds(joinTime);
        ActProcessLock actProcessLock = new ActProcessLock();
        actProcessLock.setValidTime(localDateTime);
        actProcessLock.setId(lock.getId());
        super.updateById(actProcessLock);
    }


    @Override
    public void unLock(String lockId) {
        ActProcessLock actProcessLock = new ActProcessLock();
        actProcessLock.setId(lockId);
        actProcessLock.setStatus(ProcessLockEnum.UNLOCK.getName());
        super.updateById(actProcessLock);
    }
}
