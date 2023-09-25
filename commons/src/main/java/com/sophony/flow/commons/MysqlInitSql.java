package com.sophony.flow.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * MysqlInitSql
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/17 16:54
 */
public class MysqlInitSql {


    public static final List<String> validSql = new ArrayList<String>(){{
        add("select count(*) from information_schema.TABLES where table_name = 'flow_act_procdef';");
        add("select count(*) from information_schema.TABLES where table_name = 'flow_act_process';");
        add("select count(*) from information_schema.TABLES where table_name = 'flow_act_process_lock';");
        add("select count(*) from information_schema.TABLES where table_name = 'flow_act_process_task';");
        add("select count(*) from information_schema.TABLES where table_name = 'flow_act_task_procdef';");

    }};

    public static final List<String> sqls = new ArrayList<String>() {
        {
            add("DROP TABLE IF EXISTS `flow_act_procdef`;");
            add("CREATE TABLE `flow_act_procdef`  (\n" +
                    "  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程编号-主键',\n" +
                    "  `act_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',\n" +
                    "  `act_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,\n" +
                    "  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态;1.激活/ 0.未激活 ',\n" +
                    "  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,\n" +
                    "  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',\n" +
                    "  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `expansion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展数据',\n" +
                    "  `is_deleted` smallint(0) NULL DEFAULT '0' COMMENT '是否删除 1是 0 否',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程定义;' ROW_FORMAT = Dynamic;\n");

            add("DROP TABLE IF EXISTS `flow_act_process`;");
            add("CREATE TABLE `flow_act_process`  (\n" +
                    "  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',\n" +
                    "  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',\n" +
                    "  `expansion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展数据',\n" +
                    "  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  `is_deleted` smallint(0) NULL DEFAULT '0' COMMENT '是否删除 1是 0 否',\n" +
                    "  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `act_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程编号',\n" +
                    "  `task_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目前任务',\n" +
                    "  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,\n" +
                    "  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',\n" +
                    "  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',\n" +
                    "  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调类',\n" +
                    "  `task_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务流程历史记录',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;\n");

            add("DROP TABLE IF EXISTS `flow_act_process_lock`;");
            add("CREATE TABLE `flow_act_process_lock`  (\n" +
                    "  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,\n" +
                    "  `process_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,\n" +
                    "  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 lock 上锁\\\\ unlock 解锁',\n" +
                    "  `valid_time` datetime(0) NOT NULL COMMENT '有效时间',\n" +
                    "  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点（对于同一个人有多个审核节点来说这个是有必要的）',\n" +
                    "  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',\n" +
                    "  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `expansion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展数据',\n" +
                    "  `is_deleted` smallint(0) NULL DEFAULT '0' COMMENT '是否删除 1是 0 否',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;\n");

            add("DROP TABLE IF EXISTS `flow_act_process_task`;");
            add("CREATE TABLE `flow_act_process_task`  (\n" +
                    "  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',\n" +
                    "  `processf_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程定义编号',\n" +
                    "  `process_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流行实例编号',\n" +
                    "  `task_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编号',\n" +
                    "  `task_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',\n" +
                    "  `sort` int(0) NULL DEFAULT NULL,\n" +
                    "  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态;完成/未完成',\n" +
                    "  `audit_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人',\n" +
                    "  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '批注',\n" +
                    "  `act_dept` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批部门',\n" +
                    "  `pre_task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上一个任务节点结束id，and有多个 or可能只有一个 撤回也会记录（更多信息要记录在拓展字段里面）',\n" +
                    "  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',\n" +
                    "  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',\n" +
                    "  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',\n" +
                    "  `expansion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展数据',\n" +
                    "  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  `is_deleted` smallint(0) NULL DEFAULT '0' COMMENT '是否删除 1是 0 否',\n" +
                    "  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `taskf_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义的id',\n" +
                    "  `processf_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程模板id',\n" +
                    "  `tag_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前任务的标签',\n" +
                    "  `voucher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '凭证 true 和false， 子节点有多少个就能消费多少次',\n" +
                    "  `voucher_count` int(0) NULL DEFAULT NULL COMMENT '凭证消费次数，根据子节点个数生成',\n" +
                    "  `self_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '自身记录的历史',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE,\n" +
                    "  INDEX `idx_processId`(`process_id`) USING BTREE,\n" +
                    "  INDEX `idx_taskNo`(`task_no`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程任务实例;' ROW_FORMAT = Dynamic;\n");

            add("DROP TABLE IF EXISTS `flow_act_task_procdef`;");
            add("CREATE TABLE `flow_act_task_procdef`  (\n" +
                    "  `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',\n" +
                    "  `processf_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程模板名称',\n" +
                    "  `task_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',\n" +
                    "  `task_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编号',\n" +
                    "  `sort` int(0) NOT NULL COMMENT '任务顺序',\n" +
                    "  `task_role` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务审核人/角色',\n" +
                    "  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',\n" +
                    "  `back_tasks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '退回节点，可以多节点',\n" +
                    "  `message` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,\n" +
                    "  `ret_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,\n" +
                    "  `process_fid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程模板id',\n" +
                    "  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',\n" +
                    "  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `expansion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拓展数据',\n" +
                    "  `is_deleted` smallint(0) NULL DEFAULT '0' COMMENT '是否删除 1是 0 否',\n" +
                    "  `tag_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联标签ids',\n" +
                    "  `pre_task_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上一级任务节点',\n" +
                    "  `cond` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'and / or 对于多父节点有效， 默认 and',\n" +
                    "  `interrupt_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行中断tag',\n" +
                    "  `next_task_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一级任务节点',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程任务定义表;' ROW_FORMAT = Dynamic;\n");

        }
    };


}
