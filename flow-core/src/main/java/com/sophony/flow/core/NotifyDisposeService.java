package com.sophony.flow.core;

import com.sophony.flow.common.FlowNotify;
import com.sophony.flow.common.MethodLoader;
import com.sophony.flow.common.constant.ParamKey;
import com.sophony.flow.commons.BusParam;
import com.sophony.flow.commons.annotation.FlowAuditAfter;
import com.sophony.flow.commons.annotation.FlowAuditEnd;
import com.sophony.flow.commons.annotation.FlowAuditStart;
import com.sophony.flow.event.FlowRegisterEvent;
import com.sophony.flow.mapping.ActProcessTask;
import com.sophony.flow.model.ProcessCommonModel;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
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

    @Resource
    Environment environment;



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
        BusParam.getInstance().setMap(new LinkedHashMap(){{
            put(ParamKey.CONTENTKEY, processCommonModel);
        }});
        Boolean f = environment.getProperty("yzm.flow.annotation", Boolean.class);
        Object hook = null;
        if(f){
            hook = flowNotify.getHook(true);
            if(Objects.isNull(hook)){
                return;
            }
        }

        switch (flowNotify.getNotifyEnum()){
            case END:
                if(f){
                    Method method = getMethod(hook, FlowAuditEnd.class);
                    if(Objects.isNull(method)){
                        return;
                    }
                    try {
                        method.invoke(hook);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    flowNotify.getHook().goEndBack(processCommonModel);
                }
                break;
            case START:
                if(f){
                    Method method = getMethod(hook, FlowAuditStart.class);
                    if(Objects.isNull(method)){
                        return;
                    }
                    try {
                        method.invoke(hook);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    flowNotify.getHook().start(processCommonModel);
                }
                break;
            case TASKAUDITAFTER:
                Map<String, Object> business = flowNotify.getBusiness();
                processCommonModel.afterInit((ActProcessTask)business.get("currentNode"), String.valueOf(business.get("businessParams")));
                BusParam.getInstance().setMap(new LinkedHashMap(){{
                    put(ParamKey.CONTENTKEY, processCommonModel);
                }});
                if(f){
                    Method method = getMethod(hook, FlowAuditAfter.class);
                    if(Objects.isNull(method)){
                        return;
                    }
                    try {
                        method.invoke(hook);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    flowNotify.getHook().auditAfter(processCommonModel);
                }
                break;
        }
    }



    private Method getMethod(Object o, Class<? extends Annotation> clazz){
        Method method = MethodLoader.getMethod(o.getClass().getMethods(), clazz);
        if(method == null){
            method = MethodLoader.getMethod(o.getClass().getDeclaredMethods(), clazz);
        }
        return method;
    }

}
