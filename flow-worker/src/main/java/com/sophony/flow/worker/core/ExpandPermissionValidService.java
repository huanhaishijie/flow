package com.sophony.flow.worker.core;

import com.sophony.flow.worker.base.FlowValidService;
import com.sophony.flow.worker.base.PermissionValid;
import com.sophony.flow.worker.modle.TaskNode;
import com.sophony.flow.worker.modle.TaskPermission;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ExPandPermissionValidService
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/10 16:57
 */

@Service
public class ExpandPermissionValidService  implements FlowValidService {

    private PermissionValid permissionValid;

    public PermissionValid getPermissionValid() {
        return permissionValid;
    }

    public void setPermissionValid(PermissionValid permissionValid) {
        this.permissionValid = permissionValid;
    }

    @Override
    public TaskPermission valid(List<TaskNode> taskNodeList) {
        return permissionValid.valid(taskNodeList);
    }


}
