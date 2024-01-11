package com.sophony.flow.worker.base;

import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;

import java.util.List;
import java.util.Map;

/**
 * PermissionValid
 *
 * @author yzm
 * @version 1.5.0
 * @description 权限校验
 * @date 2023/3/9 10:15
 */
public interface PermissionValid {


    /**
     * 自定义校验，使用角色去校验
     * 后续审批会使用 TaskPermission 中第一个taskNodeList 去做操作
     * 1.使用方先要对已下任务节点进行权限校验 然后过滤
     * 2.将优先级排在最前面
     * 3.后续流程流程将对taskNodeList 中第一个taskNode 进行操作
     *
     * @param taskNodeList
     * @return
     */

    TaskPermission valid(List<TaskNode> taskNodeList, Map<String, Object> params);

}
