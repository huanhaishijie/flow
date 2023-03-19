package com.sophony.flow.worker.persistence;

/**
 * CacheDo
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/19 14:09
 */
public class CacheDO {

    String key;

    String group;

    String value;

    /**
     * type 现在支持两种类型， String， Hash
     */
    String type;


    //如果为-1， 代表数据永不失效，不会被清理， 本质是当前的时间戳
    Long deathLine = -1L;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getDeathLine() {
        return deathLine;
    }

    public void setDeathLine(Long deathLine) {
        this.deathLine = deathLine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
