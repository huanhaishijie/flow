package com.sophony.flow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FlowProperties
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/8 23:04
 */
@ConfigurationProperties(prefix = "yzm.flow")
public class FlowProperties {

    private final Worker worker = new Worker();

    public Worker getWorker(){
        return worker;
    }

    private boolean joinuser;
    private boolean rolevalid;
    private boolean enable;


    public boolean isJoinuser() {
        return worker.joinUser;
    }

    public void setJoinuser(boolean joinuser) {
        worker.joinUser = joinuser;
    }

    public boolean isRolevalid() {
        return worker.roleValid;
    }

    public void setRolevalid(boolean rolevalid) {
        worker.roleValid = rolevalid;
    }

    public boolean isEnable() {
        return worker.enable;
    }

    public void setEnable(boolean enable) {
        worker.enable = enable;
    }

    public static class Worker{


        /**
         * 是否开启加入用户操作
         */
        private boolean joinUser = false;

        /**
         * 是否开启角色校验
         */
        private boolean roleValid = false;


        /**
         * 是否开启流程
         */
        private boolean enable = true;


        public boolean isJoinUser() {
            return joinUser;
        }

        public void setJoinUser(boolean joinUser) {
            this.joinUser = joinUser;
        }

        public boolean isRoleValid() {
            return roleValid;
        }

        public void setRoleValid(boolean roleValid) {
            this.roleValid = roleValid;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }


}
