package com.sophony.flow.common;

import com.sophony.flow.commons.constant.NotifyEnum;
import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.model.IProcess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FlowNotify
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 12:32
 */
public class FlowNotify {
    NotifyEnum notifyEnum;

    String processId;

    ProcessOperationEnum processOperationEnum;


    IProcess hook;

    Object objectHook;

    Map<String, Object> business = new ConcurrentHashMap<>();


    public NotifyEnum getNotifyEnum() {
        return notifyEnum;
    }

    public void setNotifyEnum(NotifyEnum notifyEnum) {
        this.notifyEnum = notifyEnum;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public IProcess getHook() {
        return hook;
    }

    public Object getHook(boolean env) {
        if(env){
            return objectHook;
        }
        return hook;
    }

    public void setHook(IProcess hook) {
        this.hook = hook;
    }

    public void setHook(Object hook) {
        this.objectHook = hook;
    }

    public ProcessOperationEnum getProcessOperationEnum() {
        return processOperationEnum;
    }

    public void setProcessOperationEnum(ProcessOperationEnum processOperationEnum) {
        this.processOperationEnum = processOperationEnum;
    }

    public Map<String, Object> getBusiness() {
        return business;
    }
}
