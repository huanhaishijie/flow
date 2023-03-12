package com.sophony.flow.config;

import com.sophony.flow.worker.FlowJobWorker;
import com.sophony.flow.worker.FlowSpringJobWorker;
import com.sophony.flow.worker.common.FlowWorkConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * FlowConfig
 *
 * @author yzm
 * @version 1.0
 * @description 启动配置
 * @date 2023/3/8 21:44
 */


@Configuration
@EnableConfigurationProperties(FlowProperties.class)
@ConditionalOnProperty(prefix = "yzm.flow", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("com.sophony.flow.**")
public class FlowConfig {


    @Bean
    public FlowJobWorker initFlow(FlowProperties properties){
        FlowProperties.Worker worker = properties.getWorker();
        FlowWorkConfig flowWorkConfig = new FlowWorkConfig();
        flowWorkConfig.setJoinUser(worker.isJoinUser());
        flowWorkConfig.setValidRole(worker.isRoleValid());

        return new FlowSpringJobWorker(flowWorkConfig);
    }
}
