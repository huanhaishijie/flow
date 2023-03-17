package com.sophony.flow.worker.modle;

import com.sophony.flow.commons.constant.ProcessTaskStateEnum;

/**
 * TaskNode
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 14:06
 */
public class TaskNode {

    /**
     * task模板id
     */
    String taskTemplateId;

    /**
     * 任务状态
     */
    ProcessTaskStateEnum state;

    /**
     * 任务名称
     */
    String taskName;

    /**
     * 任务编号
     */
    String taskNo;

    /**
     * 任务id
     */
    String id;

    public String getTaskTemplateId() {
        return taskTemplateId;
    }

    public void setTaskTemplateId(String taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
    }

    public ProcessTaskStateEnum getState() {
        return state;
    }

    public void setState(ProcessTaskStateEnum state) {
        this.state = state;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
