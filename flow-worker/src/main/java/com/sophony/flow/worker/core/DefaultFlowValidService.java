package com.sophony.flow.worker.core;

import com.sophony.flow.worker.base.FlowValidService;
import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DefaultFlowValidService
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 16:52
 */
@Service
public class DefaultFlowValidService implements FlowValidService {
    @Override
    public TaskPermission valid(List<TaskNode> taskNodeList, Map<String, Object> params) {
        TaskPermission taskPermission = new TaskPermission();
        taskPermission.setTaskNodeList(taskNodeList);
        taskPermission.setAudit(true);
        taskPermission.setWithdraw(true);
        return taskPermission;
    }
}
