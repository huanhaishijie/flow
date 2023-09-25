package com.sophony.flow.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresqlInit
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/17 16:54
 */
public class PostgresqlInitSql {

    public static final List<String> validSql = new ArrayList<String>(){{
        add("select count(*) from information_schema.tables where table_schema=? and table_name='flow_act_procdef';");
        add("select count(*) from information_schema.tables where table_schema=? and table_name='flow_act_task_procdef';");
        add("select count(*) from information_schema.tables where table_schema=? and table_name='flow_act_process';");
        add("select count(*) from information_schema.tables where table_schema=? and table_name='flow_act_process_task';");
        add("select count(*) from information_schema.tables where table_schema=? and table_name='flow_act_process_lock';");
    }};

    public static final String sql = "begin;\n" +
            "\n" +
            "\n" +
            "DROP TABLE IF EXISTS \"flow_act_task_procdef\";\n" +
            "CREATE TABLE \"flow_act_task_procdef\" (\n" +
            "  \"id\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"processf_name\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"task_name\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"task_no\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"sort\" int4 NOT NULL,\n" +
            "  \"task_role\" varchar(500) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"remark\" varchar(512) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"back_tasks\" varchar(512) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"message\" varchar(1) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"ret_url\" varchar(128) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"process_fid\" varchar(128) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"create_time\" timestamp(6),\n" +
            "  \"update_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"update_time\" timestamp(6),\n" +
            "  \"create_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"expansion\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"is_deleted\" int2 DEFAULT 0,\n" +
            "  \"tag_ids\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"pre_task_ids\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"cond\" varchar(30) COLLATE \"pg_catalog\".\"default\" DEFAULT 'and'::character varying,\n" +
            "  \"interrupt_tag\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"next_task_ids\" varchar(255) COLLATE \"pg_catalog\".\"default\"\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"id\" IS 'id';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"processf_name\" IS '流程模板名称';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"task_name\" IS '任务名称';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"task_no\" IS '任务编号';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"sort\" IS '任务顺序';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"task_role\" IS '任务审核人/角色';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"remark\" IS '备注';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"back_tasks\" IS '退回节点，可以多节点';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"process_fid\" IS '流程模板id';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"create_time\" IS '创建时间';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"update_user\" IS '更新人';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"update_time\" IS '更新时间';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"create_user\" IS '创建人';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"expansion\" IS '拓展数据';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"is_deleted\" IS '是否删除 1是 0 否';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"tag_ids\" IS '关联标签ids';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"pre_task_ids\" IS '上一级任务节点';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"cond\" IS 'and / or 对于多父节点有效， 默认 and';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"interrupt_tag\" IS '执行中断tag';\n" +
            "COMMENT ON COLUMN \"flow_act_task_procdef\".\"next_task_ids\" IS '下一级任务节点';\n" +
            "COMMENT ON TABLE \"flow_act_task_procdef\" IS '流程任务定义表;';\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Table structure for flow_act_process_task\n" +
            "-- ----------------------------\n" +
            "DROP TABLE IF EXISTS \"flow_act_process_task\";\n" +
            "CREATE TABLE \"flow_act_process_task\" (\n" +
            "  \"id\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"processf_name\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"process_id\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"task_no\" varchar(32) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"task_name\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"sort\" int4,\n" +
            "  \"state\" varchar(20) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"audit_user\" varchar(32) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"content\" varchar(512) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"act_dept\" varchar(128) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"pre_task_id\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"start_time\" timestamp(6),\n" +
            "  \"end_time\" timestamp(6),\n" +
            "  \"update_time\" timestamp(6),\n" +
            "  \"expansion\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"create_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"update_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"is_deleted\" int2 DEFAULT 0,\n" +
            "  \"create_time\" timestamp(6),\n" +
            "  \"taskf_id\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"processf_id\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"tag_ids\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"voucher\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"voucher_count\" int4,\n" +
            "  \"self_history\" varchar(2000) COLLATE \"pg_catalog\".\"default\"\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"id\" IS '主键';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"processf_name\" IS '流程定义编号';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"process_id\" IS '流行实例编号';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"task_no\" IS '任务编号';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"task_name\" IS '任务名称';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"state\" IS '状态;完成/未完成';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"audit_user\" IS '审批人';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"content\" IS '批注';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"act_dept\" IS '审批部门';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"pre_task_id\" IS '上一个任务节点结束id，and有多个 or可能只有一个 撤回也会记录（更多信息要记录在拓展字段里面）';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"start_time\" IS '开始时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"end_time\" IS '结束时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"update_time\" IS '更新时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"expansion\" IS '拓展数据';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"create_user\" IS '创建人';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"update_user\" IS '更新人';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"is_deleted\" IS '是否删除 1是 0 否';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"create_time\" IS '创建时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"taskf_id\" IS '流程定义的id';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"processf_id\" IS '流程模板id';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"tag_ids\" IS '当前任务的标签';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"voucher\" IS '凭证 true 和false， 子节点有多少个就能消费多少次';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"voucher_count\" IS '凭证消费次数，根据子节点个数生成';\n" +
            "COMMENT ON COLUMN \"flow_act_process_task\".\"self_history\" IS '自身记录的历史';\n" +
            "COMMENT ON TABLE \"flow_act_process_task\" IS '流程任务实例;';\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Table structure for flow_act_process_lock\n" +
            "-- ----------------------------\n" +
            "DROP TABLE IF EXISTS \"flow_act_process_lock\";\n" +
            "CREATE TABLE \"flow_act_process_lock\" (\n" +
            "  \"id\" varchar(255) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"process_id\" varchar(255) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"status\" varchar(10) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"valid_time\" timestamp(6) NOT NULL,\n" +
            "  \"task_id\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"create_time\" timestamp(6),\n" +
            "  \"update_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"update_time\" timestamp(6),\n" +
            "  \"create_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"expansion\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"is_deleted\" int2 DEFAULT 0\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"status\" IS '状态 lock 上锁\\ unlock 解锁';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"valid_time\" IS '有效时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"task_id\" IS '节点（对于同一个人有多个审核节点来说这个是有必要的）';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"create_time\" IS '创建时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"update_user\" IS '更新人';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"update_time\" IS '更新时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"create_user\" IS '创建人';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"expansion\" IS '拓展数据';\n" +
            "COMMENT ON COLUMN \"flow_act_process_lock\".\"is_deleted\" IS '是否删除 1是 0 否';\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Table structure for flow_act_process\n" +
            "-- ----------------------------\n" +
            "DROP TABLE IF EXISTS \"flow_act_process\";\n" +
            "CREATE TABLE \"flow_act_process\" (\n" +
            "  \"id\" varchar(100) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"update_time\" timestamp(6),\n" +
            "  \"expansion\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"create_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"update_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"is_deleted\" int2 DEFAULT 0,\n" +
            "  \"create_time\" timestamp(6),\n" +
            "  \"act_id\" varchar(200) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"task_no\" varchar(200) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"state\" varchar(20) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"start_time\" timestamp(6),\n" +
            "  \"end_time\" timestamp(6),\n" +
            "  \"class_name\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"task_history\" varchar(2000) COLLATE \"pg_catalog\".\"default\"\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"id\" IS '主键';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"update_time\" IS '更新时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"expansion\" IS '拓展数据';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"create_user\" IS '创建人';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"update_user\" IS '更新人';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"is_deleted\" IS '是否删除 1是 0 否';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"create_time\" IS '创建时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"act_id\" IS '流程编号';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"task_no\" IS '目前任务';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"start_time\" IS '开始时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"end_time\" IS '结束时间';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"class_name\" IS '回调类';\n" +
            "COMMENT ON COLUMN \"flow_act_process\".\"task_history\" IS '任务流程历史记录';\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Table structure for flow_act_procdef\n" +
            "-- ----------------------------\n" +
            "DROP TABLE IF EXISTS \"flow_act_procdef\";\n" +
            "CREATE TABLE \"flow_act_procdef\" (\n" +
            "  \"id\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"act_name\" varchar(128) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"act_no\" varchar(128) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"state\" varchar(1) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
            "  \"description\" varchar(1028) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"create_time\" timestamp(6),\n" +
            "  \"update_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"update_time\" timestamp(6),\n" +
            "  \"create_user\" varchar(255) COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"expansion\" text COLLATE \"pg_catalog\".\"default\",\n" +
            "  \"is_deleted\" int2 DEFAULT 0\n" +
            ")\n" +
            ";\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"id\" IS '流程编号-主键';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"act_name\" IS '流程名称';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"state\" IS '状态;1.激活/ 0.未激活 ';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"create_time\" IS '创建时间';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"update_user\" IS '更新人';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"update_time\" IS '更新时间';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"create_user\" IS '创建人';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"expansion\" IS '拓展数据';\n" +
            "COMMENT ON COLUMN \"flow_act_procdef\".\"is_deleted\" IS '是否删除 1是 0 否';\n" +
            "COMMENT ON TABLE \"flow_act_procdef\" IS '流程定义;';\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Primary Key structure for table flow_act_task_procdef\n" +
            "-- ----------------------------\n" +
            "ALTER TABLE \"flow_act_task_procdef\" ADD CONSTRAINT \"flow_act_task_procdef_pkey\" PRIMARY KEY (\"id\");\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Indexes structure for table flow_act_process_task\n" +
            "-- ----------------------------\n" +
            "CREATE INDEX \"idx_processId\" ON \"flow_act_process_task\" USING btree (\n" +
            "  \"process_id\" COLLATE \"pg_catalog\".\"default\" \"pg_catalog\".\"text_ops\" ASC NULLS LAST\n" +
            ");\n" +
            "CREATE INDEX \"idx_taskNo\" ON \"flow_act_process_task\" USING btree (\n" +
            "  \"task_no\" COLLATE \"pg_catalog\".\"default\" \"pg_catalog\".\"text_ops\" ASC NULLS LAST\n" +
            ");\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Primary Key structure for table flow_act_process_task\n" +
            "-- ----------------------------\n" +
            "ALTER TABLE \"flow_act_process_task\" ADD CONSTRAINT \"flow_act_process_task_pkey\" PRIMARY KEY (\"id\");\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Primary Key structure for table flow_act_process_lock\n" +
            "-- ----------------------------\n" +
            "ALTER TABLE \"flow_act_process_lock\" ADD CONSTRAINT \"flow_act_process_lock_pkey\" PRIMARY KEY (\"id\");\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Primary Key structure for table flow_act_process\n" +
            "-- ----------------------------\n" +
            "ALTER TABLE \"flow_act_process\" ADD CONSTRAINT \"flow_act_process_pkey\" PRIMARY KEY (\"id\");\n" +
            "\n" +
            "-- ----------------------------\n" +
            "-- Primary Key structure for table flow_act_procdef\n" +
            "-- ----------------------------\n" +
            "ALTER TABLE \"flow_act_procdef\" ADD CONSTRAINT \"flow_act_procdef_pkey\" PRIMARY KEY (\"id\");\n" +
            "end;";


}
