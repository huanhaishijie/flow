package com.sophony.flow.worker.modle;

import java.util.List;
import com.sophony.flow.worker.modle.TaskNode;

/**
 * TaskPermission
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 10:12
 */
public  class TaskPermission<T> {


    private boolean isWithdraw;

    private boolean isAudit;


    private T t;


    List<TaskNode> taskNodeList;


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

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public List<TaskNode> getTaskNodeList() {
        return taskNodeList;
    }

    public void setTaskNodeList(List<TaskNode> taskNodeList) {
        this.taskNodeList = taskNodeList;
    }



}
