# 演示springboot + mybatis或springjdbc集成CAT全链路监控 

### 一、主要资料参考
- 服务端部署：https://github.com/dianping/cat/blob/master/cat-doc/posts/ch4-server/README.md
- 总文档：https://github.com/dianping/cat/tree/master/cat-doc

### 二、核心步骤
#### 2.1、引入CAT客户端包：
```
 <dependency>
      <groupId>com.xmd</groupId>
      <artifactId>cat-client</artifactId>
      <version>3.0.0</version>
  </dependency>
```
- 环境规范：
```
Windows环境（具体在哪个盘下，取决于你的项目）：
新建D:/data/appdatas/cat/client.xml目录和文件
新建D:/data/applogs/cat/目录

Linux环境：
新建/data/appdatas/cat/client.xml
新建D:/data/applogs/cat/目录

注意：目录要赋予读写权限
```
client.xml文件如下：
```
<?xml version="1.0" encoding="utf-8"?>
<config xmlns:xsi="http://www.w3.org/2001/XMLSchema" xsi:noNamespaceSchemaLocation="config.xsd">
    <servers>
        <server ip="192.26.46.55" port="2280" http-port="8787" /><!--192.26.46.55为CAT服务器IP-->
    </servers>
</config>
```

- 项目目录规范：
```
../src/main/resources/META-INF/app.properties文件中添加应用服务名称，如：app.name=test-web
```

- Java接入参考：https://github.com/dianping/cat/blob/master/lib/java/README.zh-CN.md

### 2.2、服务端基本信息：
- 首次建库语句：CREATE DATABASE IF NOT EXISTS cat default charset utf8mb4 COLLATE utf8_general_ci;
- 数据库信息：192.1.1.1机器MySQL中cat库
- 应用软体：/pm/server/apache-tomcat-8.5-cat/webapps
- 基本配置信息：/data/appdatas/cat
- 应用日志信息：/data/applogs/cat
- 管理员默认信息：admin  admin

### 2.3、客户端接入：
- Java接入参考：https://github.com/dianping/cat/blob/master/lib/java/README.zh-CN.md
- 接入CAT监控的java项目目录规范：../src/main/resources/META-INF/app.properties文件中添加应用服务名称，如：app.name=test-web

### 三、服务端核心配置
- 服务端配置（比如配置本地日志、报告保留的天数）http://cat.xyz.com/cat/s/config?op=serverConfigUpdate
- 客户端路由配置http://cat.xyz.com/cat/s/config?op=routerConfigUpdate

### 四、集成
- 集成springboot使用：
```
com.ydj.test.cat.CatFilterConfigure

    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatFilter filter = new MyHttpCatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }
```
- 集成Mybatis使用：
```
com.ydj.test.cat.CatMybatisPlugin
```
- 集成springjdbc使用：
```
com.ydj.test.cat.CatJdbcTemplate

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new CatJdbcTemplate(dataSource);
    }
```