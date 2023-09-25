package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

import java.time.LocalDateTime;

/**
 * ActProcessLock
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程锁
 * @date 2023/3/7 10:36
 */
public class ActProcessLock extends BaseMappingEO {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 状态 lock 上锁\ unlock 解锁
     */
    private String status;


    /**
     * 有效到期时间  时间一过， lock状态无效化
     */
    private LocalDateTime validTime;


    /**
     * 节点（对于同一个人有多个审核节点来说这个是有必要的）
     */
    private String taskId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(LocalDateTime validTime) {
        this.validTime = validTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTableName(){
        return "flow_act_process_lock";
    }

}
