package com.sophony.flow.serivce;

import com.sophony.flow.mapping.ActProcessLock;

/**
 * IActProcessLockService
 *
 * @author yzm
 * @version 1.0
 * @description 流程锁
 * @date 2023/3/9 23:21
 */
public interface IActProcessLockService {
    ActProcessLock getLock(String processId, Long second);

    String lock(String processId, Long second);

    void joinTime(ActProcessLock lock, Long joinTime);

    void unLock(String lockId);
}
