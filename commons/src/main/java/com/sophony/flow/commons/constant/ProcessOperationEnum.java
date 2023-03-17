package com.sophony.flow.commons.constant;

/**
 * ProcessOperation
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程操作步骤
 * @date 2023/3/9 23:37
 */
public enum ProcessOperationEnum {
    APPROVE("APPROVE", "批准"),
    REFUSE("REFUSE", "拒绝"),
    WITHDRAW("WITHDRAW", "撤回");

    private String name;

    private String description;


    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    ProcessOperationEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
