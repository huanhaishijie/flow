package com.sophony.flow.worker.base;

import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;

import java.util.List;
import java.util.Map;

/**
 * FlowValidService
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 16:52
 */
public interface FlowValidService {

    TaskPermission valid(List<TaskNode> taskNodeList, Map<String, Object> params);


}
