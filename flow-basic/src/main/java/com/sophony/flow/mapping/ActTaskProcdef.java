package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

/**
 * ActTaskProcdef
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程任务节点模板
 * @date 2023/3/8 14:38
 */


public class ActTaskProcdef extends BaseMappingEO {

    /**
     *流程模板名称
     */
    private String processfName;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 任务排序
     */
    private Integer sort;

    /**
     * 任务备注
     */
    private String remark;


    /**
     * 退回节点，可以多节点
     */
    private String backTasks;

    /**
     * 流程模板id
     */
    private String processFid;


    /**
     * 上一级任务节点
     */
    private String preTaskIds;


    /**
     * 下一级任务节点
     */
    private String nextTaskIds;

    /**
     * 关联标签ids
     * tag_ids
     * @return
     */
    private String tagIds;

    /**
     *执行中断tag
     */
    private String interruptTag;


    private String cond;

    @Override
    public String getTableName() {
        return "flow_act_task_procdef";
    }


    public String getProcessfName() {
        return processfName;
    }

    public void setProcessfName(String processfName) {
        this.processfName = processfName;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBackTasks() {
        return backTasks;
    }

    public void setBackTasks(String backTasks) {
        this.backTasks = backTasks;
    }

    public String getProcessFid() {
        return processFid;
    }

    public void setProcessFid(String processFid) {
        this.processFid = processFid;
    }



    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getInterruptTag() {
        return interruptTag;
    }

    public void setInterruptTag(String interruptTag) {
        this.interruptTag = interruptTag;
    }


    public String getPreTaskIds() {
        return preTaskIds;
    }

    public void setPreTaskIds(String preTaskIds) {
        this.preTaskIds = preTaskIds;
    }

    public String getNextTaskIds() {
        return nextTaskIds;
    }

    public void setNextTaskIds(String nextTaskIds) {
        this.nextTaskIds = nextTaskIds;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }
}
