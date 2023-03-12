package com.sophony.flow.worker.core;

import com.sophony.flow.worker.base.FlowValidService;
import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DefaultFlowValidService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/10 16:52
 */
@Service
public class DefaultFlowValidService implements FlowValidService {
    @Override
    public TaskPermission valid(List<TaskNode> taskNodeList) {
        TaskPermission taskPermission = new TaskPermission();
        taskPermission.setTaskNodeList(taskNodeList);
        taskPermission.setAudit(true);
        taskPermission.setWithdraw(true);
        return taskPermission;
    }
}
