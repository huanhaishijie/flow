package com.sophony.flow.worker.common;

import com.sophony.flow.commons.BusParam;
import com.sophony.flow.commons.MysqlInitSql;
import com.sophony.flow.commons.PostgresqlInitSql;
import com.sophony.flow.worker.base.DataService;
import com.sophony.flow.worker.base.FlowUserInfo;
import com.sophony.flow.worker.base.FlowValidService;
import com.sophony.flow.worker.base.PermissionValid;
import com.sophony.flow.worker.core.DefaultFlowValidService;
import com.sophony.flow.worker.core.DefaultService;
import com.sophony.flow.worker.core.ExpandPermissionValidService;
import com.sophony.flow.worker.core.ExpandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * PropertiesVaild
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/9 13:18
 */
@Slf4j
public class PropertiesValid {

    public PropertiesValid(FlowWorkConfig flowWorkConfig, FlowBeanFactory flowBeanFactory){
        FlowValidService flowValidService = null;
        if(flowWorkConfig.isValidRole()){
            try {
                PermissionValid bean = flowBeanFactory.getBean(PermissionValid.class);
                ExpandPermissionValidService expandPermissionValidService = flowBeanFactory.getBean(ExpandPermissionValidService.class);
                expandPermissionValidService.setPermissionValid(bean);
                flowValidService = expandPermissionValidService;
            }catch (Exception e) {
                e.printStackTrace();
                log.info("no impl PermissionValid 没有实现角色校验");
                Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("no impl PermissionValid 没有实现角色校验")));
                flowBeanFactory.shutdown();
            }
        }else {
            flowValidService = flowBeanFactory.getBean(DefaultFlowValidService.class);
        }
        flowBeanFactory.setFlowValidService(flowValidService);

        DataService dataService = null;
        if(flowWorkConfig.isJoinUser()){
            try {
                FlowUserInfo bean = flowBeanFactory.getBean(FlowUserInfo.class);
                ExpandService expandService = flowBeanFactory.getBean(ExpandService.class);
                expandService.setFlowUserInfo(bean);
                dataService = expandService;
            }catch (Exception e){
                e.printStackTrace();
                log.info("没有实现FlowUserInfo");
                Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("没有实现FlowUserInfo")));
                flowBeanFactory.shutdown();
            }
        }else {
            dataService = flowBeanFactory.getBean(DefaultService.class);
        }
        FlowBeanFactory.getInstance().setDataService(dataService);
        PostgreSql postgreSql = new PostgreSql();
        postgreSql.setJdbcTemplate(FlowBeanFactory.getInstance().getBean(JdbcTemplate.class));
        Mysql mysql = new Mysql();
        mysql.setJdbcTemplate(FlowBeanFactory.getInstance().getBean(JdbcTemplate.class));
        Map<String, Object> setting = new ConcurrentHashMap<>();
        if(StringUtils.isEmpty(flowWorkConfig.getSqlType())){
            DataSource dataSource = FlowBeanFactory.getInstance().getBean(JdbcTemplate.class).getDataSource();
            try {
                Method getDriverClassName = dataSource.getClass().getMethod("getDriverClassName");
                String res = String.valueOf(getDriverClassName.invoke(dataSource));
                if(StringUtils.equals(res, "org.postgresql.Driver")){

                    setting.put("sqlType", "postgresql");
                    setting.put("SqlInit", postgreSql);
                }else {
                    setting.put("sqlType", "mysql");
                    setting.put("SqlInit", mysql);
                }
            }catch (Exception e){
                log.info("未知当前数据库类型");
            }
        }else {
            if(flowWorkConfig.getSqlType().equals("mysql")){
                setting.put("sqlType", "mysql");
                setting.put("SqlInit", mysql);
            }else {
                setting.put("sqlType", "postgresql");
                setting.put("SqlInit", postgreSql);
            }
        }
        BusParam.getInstance().setMap(setting);
    }


}
