package com.ydj.springtest.service.user.controller;

import com.dianping.cat.Cat;
import com.ydj.springtest.service.user.dao.entity.UserInfoEntity;
import com.ydj.springtest.service.user.service.UserService;
import com.ydj.springtest.utils.HttpKit;
import com.ydj.springtest.utils.SysProperties;
import com.ydj.test.wxpush.SendWeChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author : Ares.yi
 * @createTime : 2019-1-19
 * @version : 1.0
 * @description :
 *
 */
@RestController
@EnableAutoConfiguration
public class HelloController {

    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    private List<UserInfoEntity> index(){

        List<UserInfoEntity> list =  this.userService.getUserList();
        this.userService.testSpringjdbc();

        return list;
    }

    @RequestMapping("/index2")
    private List<UserInfoEntity> index2(){
        List<UserInfoEntity> list =  this.userService.getUserList();

        try {
            int i = 5/0;
        } catch (Exception e) {
            e.printStackTrace();
            Cat.logError(e);
            Cat.logMetricForCount("zeroException");
        }
        return list;
    }

    @RequestMapping("/index3")
    private String index3(){
        String s = HttpKit.getHtmlContent("http://localhost:8080/springtest/index?CONSUMER-APP-NAME=springtestMyself","test");

        s = HttpKit.getHtmlContent("http://localhost:8080/springtest/index4?CONSUMER-APP-NAME=springtestMyself2","test");
        return s;
    }

    @RequestMapping("/index4")
    private String index4(){
        this.userService.testSpringjdbc();
        return SysProperties.getAppName();
    }

    /**
     * 企业微信测试
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
