package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

import java.time.LocalDateTime;

/**
 * ActProcess
 *
 * @author yzm
 * @version 1.0
 * @description 流程实例
 * @date 2023/3/8 14:07
 */

public class ActProcess extends BaseMappingEO {


    /**
     * 流程模板id
     */
    private String actId;

    /**
     * 当前任务节点
     */
    private String taskNo;


    /**
     * 任务状态
     */
    private String state;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 任务回调类
     */
    private String className;


    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String getTableName() {
        return "act_process";
    }
}
