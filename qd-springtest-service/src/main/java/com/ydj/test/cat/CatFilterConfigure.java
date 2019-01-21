package com.ydj.test.cat;

//import com.dianping.cat.servlet.CatFilter;
import com.ydj.test.cat.integration.my.MyHttpCatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author : Ares.yi
 * @createTime : 2019-1-19
 * @version : 1.0
 * @description :
 *
 */
@Configuration
public class CatFilterConfigure {

    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
//        CatFilter filter = new CatFilter();
        MyHttpCatFilter filter = new MyHttpCatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }
}