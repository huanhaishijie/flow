package com.sophony.flow.worker.common;

/**
 * FlowConfig
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 12:52
 */
public class FlowWorkConfig {

    private boolean validRole;

    private boolean joinUser;

    private String sqlType;


    public boolean isValidRole() {
        return validRole;
    }

    public void setValidRole(boolean validRole) {
        this.validRole = validRole;
    }

    public boolean isJoinUser() {
        return joinUser;
    }

    public void setJoinUser(boolean joinUser) {
        this.joinUser = joinUser;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }
}
