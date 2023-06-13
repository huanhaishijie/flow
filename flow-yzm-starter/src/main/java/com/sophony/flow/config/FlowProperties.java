package com.sophony.flow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FlowProperties
 *
 * @author yzm
 * @version 1.5.0
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
    private boolean annotation;

    private String sqlType;

    private boolean openCache;

    private String cacheType;



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

    public void setSqlType(String sqlType){
        worker.sqlType = sqlType;
    }

    public String getSqlType(){
        return worker.getSqlType();
    }

    public void setOpenCache(boolean openCache){
        worker.openCache = openCache;
    }

    public boolean isOpenCache(){
        return worker.openCache;
    }


    public void setCacheType(String cacheType){
        worker.cacheType = cacheType;
    }

    public String getCacheType() {
        return worker.cacheType;
    }


    public boolean isAnnotation() {
        return annotation;
    }

    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
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


        /**
         * 是否开启注解
         */
        private boolean annotation = false;

        private String sqlType;


        /**
         * 是否开启缓存
         */
        private boolean openCache = true;

        /**
         * 缓存类型， 目前支持H2， redis
         */
        private String cacheType = "H2";


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

        public String getSqlType() {
            return sqlType;
        }

        public void setSqlType(String sqlType) {
            this.sqlType = sqlType;
        }


        public boolean isOpenCache() {
            return openCache;
        }

        public void setOpenCache(boolean openCache) {
            this.openCache = openCache;
        }

        public String getCacheType() {
            return cacheType;
        }

        public void setCacheType(String cacheType) {
            this.cacheType = cacheType;
        }


        public void setAnnotation(boolean annotation) {
            this.annotation = annotation;
        }

        public boolean getAnnotation() {
            return annotation;
        }
    }


}
