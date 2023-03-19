package com.sophony.flow.worker.modle;

import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.model.IProcess;
import com.sophony.flow.worker.cache.FlowCacheService;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.core.ExpandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * ProcessModel
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程抽象核心Model
 * @date 2023/3/10 0:14
 */


public abstract class ProcessModel implements IProcess {


    private String key = "PROCESS";

    private String processId;

    private boolean isCleanCache;

    private ProcessOperationEnum operation;

    private TaskNode taskNode;

    private boolean isWithdraw = false;

    private boolean isAudit = false;


    private Object t;





    public ProcessModel(String processId){
        this.processId = processId;
        construction();
    }

    public ProcessModel(String processId, ProcessOperationEnum operation, boolean isCleanCache){
        this.processId = processId;
        this.operation = operation;
        this.isCleanCache = isCleanCache;
        if(isCleanCache){
            FlowCacheService cacheService = FlowBeanFactory.getInstance().getBean(FlowCacheService.class);
            ExpandService expandService = FlowBeanFactory.getInstance().getBean(ExpandService.class);
            User currentUser = expandService.getCurrentUser();
            String userId = "None";
            if(Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getUserId())){
                userId = currentUser.getUserId();
            }

            cacheService.hdel(key + userId, processId);
        }
        this.construction();
    }




    public ProcessModel(){
        construction();
    }


    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;

    }

    @Override
    public IProcess construction() {
        if(StringUtils.isEmpty(this.getProcessId())){
            return this;
        }
        FlowCacheService cacheService = FlowBeanFactory.getInstance().getBean(FlowCacheService.class);
        ExpandService expandService = FlowBeanFactory.getInstance().getBean(ExpandService.class);
        User currentUser = expandService.getCurrentUser();
        String userId = "None";
        if(Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getUserId())){
            userId = currentUser.getUserId();
        }
        ProcessModel processModel = cacheService.hget(key + userId, processId, this.getClass());
        if(Objects.nonNull(processModel)){
            this.setAudit(processModel.isAudit());
            this.setWithdraw(processModel.isWithdraw());
            this.setTaskNode(processModel.getTaskNode());
            return this;
        }
        init();
        cacheService.hset(key + userId, processId, this, 5 * 60L);
        return this;
    }

    public abstract void init();




    public ProcessOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(ProcessOperationEnum operation) {
        this.operation = operation;
    }


    public boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(boolean withdraw) {
        isWithdraw = withdraw;
    }

    public boolean isAudit() {
        return isAudit;
    }

    public void setAudit(boolean audit) {
        isAudit = audit;
    }

    public Object getT() {
        return t;
    }

    public void setT(Object t) {
        this.t = t;
    }

    public TaskNode getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(TaskNode taskNode) {
        this.taskNode = taskNode;
    }
}
