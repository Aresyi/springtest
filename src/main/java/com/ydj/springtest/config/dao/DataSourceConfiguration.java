package com.ydj.springtest.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ydj.springtest.CatJdbcTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**测试两种方式建立连接池*/
@Configuration
@MapperScan("com.ydj.springtest.dao")
public class DataSourceConfiguration {

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与spring-dao.xml对应的bean  dataSource
     * @return
     */
    @Bean(name = "myBatisDataSource")
    @Qualifier("myBatisDataSource")
    @Primary
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        //创建DataSource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        //配置c3p0连接池的私有属性
        //连接池的最大线程数量
        dataSource.setMaxPoolSize(30);
        //连接池的最小线程数量
        dataSource.setMinPoolSize(30);
        //连接池不自动commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间
        dataSource.setCheckoutTimeout(10000);
        //连接失败自动重试次数
        dataSource.setAcquireRetryAttempts(2);

        return dataSource;
    }


    @Bean(name = "jdbcDataSource")
    @Qualifier("jdbcDataSource")
    @ConfigurationProperties(prefix="pmOld.db")
    public DataSource jdbcDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "catJdbcTemplateTest")
    public JdbcTemplate catJdbcTemplateTest(@Qualifier("jdbcDataSource") DataSource dataSource) {
        return new CatJdbcTemplate(dataSource);
    }


}
