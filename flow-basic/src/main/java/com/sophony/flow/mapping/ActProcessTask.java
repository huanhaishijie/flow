package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

import java.time.LocalDateTime;

/**
 * ActProcessTask
 *
 * @author yzm
 * @version 1.0
 * @description 节点任务实例表
 * @date 2023/3/8 15:55
 */
public class ActProcessTask extends BaseMappingEO {

    /**
     * 流程模板定义编号
     */
    private String processfName;



    /**
     * 任务编号
     */
    private String taskNo;


    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 状态 run 进行中 back 退回  withdraw 撤回  interrupt 中断（ 同组标签，有一个审核节点回退，其它节点中断， 中断节点需记录是因哪个节点审核回退而导致中断的）
     */
    private String state;


    /**
     * 审批人
     */
    private String auditUser;

    /**
     * 批注
     */
    private String content;

    /**
     * 上一个任务节点结束id，and有多个 or可能只有一个 撤回也会记录（更多信息要记录在拓展字段里面）
     */
    private String preTaskId;



    /**
     * 开始时间
     */
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 任务节点定义的id
     */
    private String taskfId;

    /**
     * 流程模板定义id
     */
    private String processfId;

    /**
     * 流程实例id
     */
    private String processId;

    /**
     * 当前任务的标签ids
     */
    private String tagIds;


    /**
     * 凭证 true 和false， 子节点有多少个就能消费多少次
     */
    private String voucher;

    /**
     * 凭证消费次数， 归零voucher将作废
     */
    private Integer voucherCount;

    /**
     * 节点本身的历史
     */
    private String selfHistory;





    @Override
    public String getTableName() {
        return "act_process_task";
    }


    public String getProcessfName() {
        return processfName;
    }

    public void setProcessfName(String processfName) {
        this.processfName = processfName;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreTaskId() {
        return preTaskId;
    }

    public void setPreTaskId(String preTaskId) {
        this.preTaskId = preTaskId;
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

    public String getTaskfId() {
        return taskfId;
    }

    public void setTaskfId(String taskfId) {
        this.taskfId = taskfId;
    }

    public String getProcessfId() {
        return processfId;
    }

    public void setProcessfId(String processfId) {
        this.processfId = processfId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Integer getVoucherCount() {
        return voucherCount;
    }

    public void setVoucherCount(Integer voucherCount) {
        this.voucherCount = voucherCount;
    }

    public String getSelfHistory() {
        return selfHistory;
    }

    public void setSelfHistory(String selfHistory) {
        this.selfHistory = selfHistory;
    }
}
