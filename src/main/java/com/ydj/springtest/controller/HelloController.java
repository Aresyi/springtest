package com.ydj.springtest.controller;


import com.dianping.cat.Cat;
import com.ydj.springtest.CatJdbcTemplate;
import com.ydj.springtest.entity.Area;
import com.ydj.springtest.service.AreaService;
import com.ydj.push.wx.SendWeChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class HelloController {

    @Autowired
    private AreaService areaService;

    @Resource(name = "catJdbcTemplateTest")
    private JdbcTemplate catJdbcTemplateTest;

    @RequestMapping("/index")
    private List<Area> index(){
        List<Area> list =  this.areaService.getAreaList();

        catJdbcTemplateTest.query(
                "select name FROM ad_base_info where id = ? limit 1",
                new Object[] {152}, new RowCallbackHandler() {
                    public void processRow(ResultSet rs)
                            throws SQLException {
                        System.out.println(rs.getString("name"));
                    }
                });

        catJdbcTemplateTest.update("update ad_base_info set id = 152 where id=152");

        List<String> strings = catJdbcTemplateTest.queryForList("select name FROM ad_base_info limit 5", String.class);

        return list;
    }

    @RequestMapping("/index2")
    private List<Area> index2(){
        List<Area> list =  this.areaService.getAreaList();

        try {
            int i = 5/0;
        } catch (Exception e) {
            e.printStackTrace();
            Cat.logError(e);
            Cat.logMetricForCount("zeroException");
        }
        return list;
    }

    /**
     * 发送微信方法
     *
     **/
    @RequestMapping("/sendWeChat")
    public void send(HttpServletRequest request) {
        System.out.println("request = [" + request + "]");
        Map<String, String[]> map = request.getParameterMap();

        String[] emails = new String[]{"qy01b0aa7b9347ba009ba730c6b7"};

        SendWeChatUtils msgUtils = new SendWeChatUtils();
        try {
            String postdata = msgUtils.createPostData(msgUtils.toUser(emails), "text", "content", "contentValue");
            String resp = msgUtils.post("utf-8", "text", postdata);
            System.out.println("请求数据======>" + postdata);
            System.out.println("发送微信的响应数据======>" + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
