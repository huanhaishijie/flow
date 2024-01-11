package com.sophony.flow.worker.modle;

import com.sophony.flow.commons.BusParam;
import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.model.IProcess;
import com.sophony.flow.mapping.ActProcessTask;
import com.sophony.flow.worker.base.DataService;
import com.sophony.flow.worker.cache.FlowCacheService;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.core.ExpandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Map;
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

    private String processTemplateId;

    private boolean isCleanCache;

    protected ProcessOperationEnum operation;

    private TaskNode taskNode;

    private boolean isWithdraw = false;

    private boolean isAudit = false;


    private Object t;

    private ActProcessTask finishNode;

    private String businessParams;





    public ProcessModel(String processId){
        this.processId = processId;
        construction();
    }

    public ProcessModel(String processId, ProcessOperationEnum operation, boolean isCleanCache){
        this.processId = processId;
        this.operation = operation;
        this.isCleanCache = isCleanCache;
        if(isCleanCache){
            String userId = "None";
            FlowCacheService cacheService = FlowBeanFactory.getInstance().getBean(FlowCacheService.class);
            DataService expandService = FlowBeanFactory.getInstance().getDataService();
            User currentUser = null;
            if(Objects.nonNull(expandService)){
                currentUser = expandService.getCurrentUser();
            }
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
        String userId = "None";
        FlowCacheService cacheService = FlowBeanFactory.getInstance().getBean(FlowCacheService.class);
        DataService expandService = FlowBeanFactory.getInstance().getDataService();
        User currentUser = null;
        if(Objects.nonNull(expandService)){
            currentUser = expandService.getCurrentUser();
        }
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

    public ActProcessTask getFinishNode() {
        return finishNode;
    }

    public void setFinishNode(ActProcessTask finishNode) {
        this.finishNode = finishNode;
    }

    public String getBusinessParams() {
        return businessParams;
    }

    public void setBusinessParams(String businessParams) {
        this.businessParams = businessParams;
    }


    public void afterInit(ActProcessTask finishNode, String businessParams){
        this.finishNode = finishNode;
        this.businessParams = businessParams;
    }

    public String getProcessTemplateId() {
        return processTemplateId;
    }

    public void setProcessTemplateId(String processTemplateId) {
        this.processTemplateId = processTemplateId;
    }
}
