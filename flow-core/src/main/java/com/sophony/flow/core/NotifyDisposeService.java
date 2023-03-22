package com.sophony.flow.core;

import com.sophony.flow.common.FlowNotify;
import com.sophony.flow.event.FlowRegisterEvent;
import com.sophony.flow.mapping.ActProcessTask;
import com.sophony.flow.model.ProcessCommonModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.Objects;

/**
 * NotifyDisposeService
 *
 * @author yzm
 * @version 1.5.0
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
        ProcessCommonModel processCommonModel = new ProcessCommonModel();
        processCommonModel.setProcessId(flowNotify.getProcessId());
        processCommonModel.setOperation(flowNotify.getProcessOperationEnum());

        switch (flowNotify.getNotifyEnum()){
            case END:
                flowNotify.getHook().goEndBack(processCommonModel);
                break;
            case START:
                flowNotify.getHook().start(processCommonModel);
                break;
            case TASKAUDITAFTER:
                Map<String, Object> business = flowNotify.getBusiness();
                processCommonModel.afterInit((ActProcessTask)business.get("currentNode"), String.valueOf(business.get("businessParams")));
                flowNotify.getHook().auditAfter(processCommonModel);
                break;
        }
    }

}
