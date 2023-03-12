package com.sophony.flow.common.aop;

import com.sophony.flow.mapping.ActProcessLock;
import com.sophony.flow.serivce.IActProcessLockService;
import io.swagger.models.auth.In;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * FLowLockAspect
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/10 18:31
 */

@Aspect
@Component
public class FLowLockAspect {


    @Resource
    IActProcessLockService actProcessLockService;


    private Long second = 60L;

    private Long joinTime = 10L;


    @Pointcut("@annotation(com.sophony.flow.annotation.FLowLock)")
    public void excuteService() {

    }

    @Before("excuteService()")
    public void before(JoinPoint joinPoint) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object arg = joinPoint.getArgs()[0];
        String processId = getProcessId(arg);
        if(processId == null){
            throw new RuntimeException("业务繁忙");
        }
        ActProcessLock lock = actProcessLockService.getLock(processId, second);
        if(Objects.isNull(lock)){
            String lockId = actProcessLockService.lock(processId, second);
        }else {
            actProcessLockService.joinTime(lock, joinTime);
            throw new RuntimeException("业务繁忙");
        }
    }

    @After("excuteService()")
    public void after(JoinPoint joinPoint) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object arg = joinPoint.getArgs()[0];
        String processId = getProcessId(arg);
        if(processId == null){
            throw new RuntimeException("业务繁忙");
        }
        ActProcessLock lock = actProcessLockService.getLock(processId, second);
        if(Objects.nonNull(lock)){
           actProcessLockService.unLock(lock.getId());
        }
    }


//    @Around("excuteService()")

    public Object doAroundMethod(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {

            Object arg = joinPoint.getArgs()[0];

            String processId = getProcessId(arg);
            if(processId == null){
                throw new RuntimeException("业务繁忙");
            }
            ActProcessLock lock = actProcessLockService.getLock(processId, second);
            if(Objects.isNull(lock)){
                String lockId = actProcessLockService.lock(processId, second);
                object = joinPoint.proceed();
                actProcessLockService.unLock(lockId);
            }else {
                actProcessLockService.joinTime(lock, joinTime);
                throw new RuntimeException("业务繁忙");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return object;

    }




    String getProcessId(Object arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = arg.getClass().getMethod("getProcessId").invoke(arg);
        if(Objects.isNull(obj)){
            return null;
        }
        return String.valueOf(obj);

    }






}
