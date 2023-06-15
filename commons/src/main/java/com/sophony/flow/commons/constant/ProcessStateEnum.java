package com.sophony.flow.commons.constant;

import java.util.Arrays;
import java.util.Objects;

/**
 * ProcessState
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 23:46
 */
public enum ProcessStateEnum {

    RUN("RUN", "进行中"),

    START("START", "流程开始"),
    END("END", "结束"),
    INITEND("INITEND", "初始结束"), //第一个节点审核不通过送还
    CLOSE("CLOSE", "CLOSE"), //流程异常关闭
    ;

    private String name;

    private String description;


    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    ProcessStateEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ProcessStateEnum getByName(String name){
        return Arrays.asList(ProcessStateEnum.values()).stream().filter(it -> Objects.equals(name, it.getName())).findFirst().get();
    }





}
