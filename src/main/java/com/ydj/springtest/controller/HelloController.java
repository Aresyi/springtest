package com.ydj.springtest.controller;


import com.ydj.springtest.entity.Area;
import com.ydj.springtest.service.AreaService;
import com.ydj.push.wx.SendWeChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class HelloController {

    @Autowired
    private AreaService areaService;

    @RequestMapping("/index")
    private List<Area> index(){
        List<Area> list =  this.areaService.getAreaList();

        return list;
    }


    /**
     * 发送微信方法
     *
     **/
    @RequestMapping("/sendWeChat")
    public void send(HttpServletRequest request) {
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
