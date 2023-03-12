package com.sophony.flow.worker.modle;

import com.sophony.flow.commons.constant.ProcessOperationEnum;
import com.sophony.flow.commons.model.IProcess;
import com.sophony.flow.worker.common.FlowBeanFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * ProcessModel
 *
 * @author yzm
 * @version 1.0.0
 * @description 流程抽象核心Model
 * @date 2023/3/10 0:14
 */


public abstract class ProcessModel implements IProcess {

    private FlowBeanFactory flowBeanFactory = FlowBeanFactory.getInstance();

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


        //是否清除缓存
        //是否清除缓存
        //是否清除缓存
        //是否清除缓存
        //是否清除缓存
        //是否清除缓存
        //是否清除缓存

        if(isCleanCache){

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



    /**
     *             ⚠
     *           ⚠ ⚠ ⚠
     *         ⚠   ⚠    ⚠
     *       ⚠     ⚠      ⚠
     *     ⚠                ⚠
     *   ⚠         O          ⚠
     * ⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠
     *  ⚠这一层后续要做缓存设计⚠
     *  ⚠这一层后续要做缓存设计
     *  ⚠这一层后续要做缓存设计
     *  这一层后续要做缓存设计
     *  这一层后续要做缓存设计
     *  这一层后续要做缓存设计
     *  这一层后续要做缓存设计
     *  这一层后续要做缓存设计
     *
     */

    @Override
    public IProcess construction() {
        if(StringUtils.isEmpty(this.getProcessId())){
            return this;
        }
        init();
        return this;
    }

    public abstract void init();


    public FlowBeanFactory getFlowBeanFactory() {
        return flowBeanFactory;
    }


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
