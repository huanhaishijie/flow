package com.sophony.flow.commons.constant;

/**
 * ProcessLockEnum
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 19:08
 */
public enum ProcessLockEnum {

    LOCK("lock", "上锁"),
    UNLOCK("unlock", "解锁"),
    ;


    private String name;

    private String description;



    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    ProcessLockEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
