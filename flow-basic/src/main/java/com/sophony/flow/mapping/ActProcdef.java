package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

/**
 * ActProcdef
 *
 * @author yzm
 * @version 1.5.0
 * @description 流程定义
 * @date 2023/3/8 13:59
 */
public class ActProcdef extends BaseMappingEO {

    /**
     * 流程名称
     */
    private String actName;

    /**
     * 流程编号
     */
    private String actNo;

    /**
     * 状态;1.激活/ 0.未激活
     */
    private String state;

    /**
     * 流程描述
     */
    private String description;


    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getTableName() {
        return "act_procdef";
    }
}
