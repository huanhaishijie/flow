package com.sophony.flow.commons.constant;

import java.util.Arrays;
import java.util.Objects;

/**
 * ProcessTaskEnum
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 23:52
 */
public enum ProcessTaskStateEnum {

    RUN("RUN", "进行中"),
    FINISH("FINISH", "审核完成"),
    BACK("BACK", "回退"),
    INTERRUPTED("INTERRUPTED", "中断"),
    ;

    private String name;

    private String description;


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    ProcessTaskStateEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ProcessTaskStateEnum getByName(String name){
        return Arrays.asList(ProcessTaskStateEnum.values()).stream().filter(it -> Objects.equals(name, it.name)).findFirst().get();
    }
}
