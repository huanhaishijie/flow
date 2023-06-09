package com.sophony.flow.worker;

import com.sophony.flow.commons.BusParam;
import com.sophony.flow.worker.cache.FlowCacheService;
import com.sophony.flow.worker.common.FlowBeanFactory;
import com.sophony.flow.worker.common.FlowWorkConfig;
import com.sophony.flow.worker.common.PropertiesValid;
import com.sophony.flow.worker.common.SqlInitExecute;
import com.sophony.flow.worker.persistence.CachePersistenceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * FlowSpringJobWorker
 *
 * @author yzm
 * @version 1.5.0
 * @description Spring 项目中的 Worker 启动器
 * 能够获取到由 Spring IOC 容器管理的 processor
 * @date 2023/3/8 23:47
 */
public class FlowSpringJobWorker extends FlowJobWorker implements ApplicationContextAware, InitializingBean, DisposableBean {


    public FlowSpringJobWorker(FlowWorkConfig flowWorkConfig) {
        super(flowWorkConfig);
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        new PropertiesValid(flowWorkConfig, FlowBeanFactory.getInstance());
        SqlInitExecute sqlInit = (SqlInitExecute)BusParam.getInstance().getMap().get("SqlInit");
        sqlInit.execute();

        //存储化
        FlowBeanFactory.getInstance().getBean(FlowCacheService.class).init();




    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FlowBeanFactory.getInstance().setApplicationContext(applicationContext);

    }
}
