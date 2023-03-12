package com.sophony.flow.mapping;

import com.sophony.flow.absEo.BaseMappingEO;

/**
 * ActProcdefTag
 *
 * @author yzm
 * @version 1.0
 * @description 流程模板标签
 * @date 2023/3/8 15:45
 */
public class ActProcdefTag extends BaseMappingEO {

    /**
     * 流程模板id
     */
    private String processfId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 0未激活 1激活
     */

    private String state;


    public String getProcessfId() {
        return processfId;
    }

    public void setProcessfId(String processfId) {
        this.processfId = processfId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getTableName() {
        return "act_procdef_tag";
    }
}
