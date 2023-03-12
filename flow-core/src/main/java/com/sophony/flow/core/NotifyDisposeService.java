package com.sophony.flow.core;

import com.sophony.flow.common.FlowNotify;
import com.sophony.flow.event.FlowRegisterEvent;
import com.sophony.flow.model.ProcessCommonModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * NotifyDisposeService
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/10 12:41
 */

@Service
public class NotifyDisposeService {



    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#flowRegisterEvent.getState().toString() == 'SUCCEED'", classes = FlowRegisterEvent.class)
    public void notifyDispose(FlowRegisterEvent flowRegisterEvent){
        FlowNotify flowNotify = flowRegisterEvent.getFlowNotify();
        if(Objects.isNull(flowNotify.getHook())){
            return;
        }
        switch (flowNotify.getNotifyEnum()){
            case END:
                flowNotify.getHook().goEndBack(new ProcessCommonModel(flowNotify.getProcessId()));
                break;
            case START:
                flowNotify.getHook().start(new ProcessCommonModel(flowNotify.getProcessId()));
                break;
            case TASKAUDITAFTER:
                flowNotify.getHook().auditAfter(new ProcessCommonModel(flowNotify.getProcessId()));
                break;
        }
    }

}
