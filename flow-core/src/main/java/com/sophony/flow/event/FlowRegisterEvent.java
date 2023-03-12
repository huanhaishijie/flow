package com.sophony.flow.event;

import com.sophony.flow.common.FlowNotify;
import com.sophony.flow.worker.common.BaseEvent;

/**
 * FlowEvent
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/10 12:16
 */
public class FlowRegisterEvent extends BaseEvent {

    FlowNotify flowNotify;

    public FlowRegisterEvent(FlowNotify flowNotify) {
        super(flowNotify);
        this.setState("SUCCEED");
        this.flowNotify = flowNotify;
    }

    public FlowNotify getFlowNotify() {
        return flowNotify;
    }
}
