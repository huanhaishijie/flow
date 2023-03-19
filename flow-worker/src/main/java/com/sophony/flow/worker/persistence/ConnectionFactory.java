package com.sophony.flow.worker.persistence;

import com.sophony.flow.commons.BusParam;
import com.sophony.flow.commons.SnowFlakeWorker;
import com.sophony.flow.commons.utils.JavaUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.h2.Driver;


import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

/**
 * ConnectionFactory
 *
 * @author yzm
 * @version 1.0
 * @description h2缓存连接
 * @date 2023/3/19 13:02
 */
@Slf4j
public class ConnectionFactory {

    private volatile DataSource dataSource;

    private final String H2_PATH = System.getProperty("user.home").concat("flow/worker") + "/h2/" + SnowFlakeWorker.idGenerate() + "/";

    private final String MEMORY_JDBC_URL = String.format("jdbc:h2:mem:%sflow_worker_db;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", H2_PATH);


    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public synchronized void initDatasource(){
        Map<String, Object> busParam = BusParam.getInstance().getMap();
        if(Objects.isNull(busParam)){
            log.info("h2初始化失败, 获取不到参数");
        }
        log.info("[flowDataSource] H2 database version: {}", JavaUtils.determinePackageVersion(Driver.class));
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(Driver.class.getName());
        config.setJdbcUrl(MEMORY_JDBC_URL);
        config.setAutoCommit(true);
        // 池中最小空闲连接数量
        config.setMinimumIdle(2);
        // 池中最大连接数量
        config.setMaximumPoolSize(32);
        dataSource = new HikariDataSource(config);
        //jvm 退出删除数据库
        try {
            File file = new File(H2_PATH);
            FileUtils.forceDeleteOnExit(file);
        }catch (Exception e){
        }

    }






}
