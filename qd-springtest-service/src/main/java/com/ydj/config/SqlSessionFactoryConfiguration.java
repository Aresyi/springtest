package com.ydj.config;


import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
public class SqlSessionFactoryConfiguration{

    @Autowired
    public DataSource dataSource;
    @Autowired
    public Interceptor catMybatisPlugin;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasPackage;

    private static String mybatisConfigFile;
    private static String mapperPath;

    @Value("${mybatis.config-location}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SqlSessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }


    @Value("${mybatis.mapper-locations}")
    public void setMapperPath(String mapperPath) {
        SqlSessionFactoryConfiguration.mapperPath = mapperPath;
    }



    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);

        Interceptor[] interceptors = new Interceptor[]{catMybatisPlugin};
        sqlSessionFactoryBean.setPlugins(interceptors);

        return sqlSessionFactoryBean;
    }

}
