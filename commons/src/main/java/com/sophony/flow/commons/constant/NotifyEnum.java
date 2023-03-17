package com.sophony.flow.commons.constant;

/**
 * NotifyEnum
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 12:25
 */
public enum NotifyEnum {

    START("START", "流程开始"),
    END("END", "流程结束"),
    TASKAUDITAFTER("TASKAUDITAFTER", "任务节点审核后通知"),
    ;

    private String name;

    private String description;


    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    NotifyEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
