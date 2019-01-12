package com.ydj.springtest.service.impl;

import com.ydj.springtest.dao.AreaDao;
import com.ydj.springtest.entity.Area;
import com.ydj.springtest.service.AreaService;
import com.ydj.springtest.utils.HttpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {



    @Autowired
    private AreaDao areaDao;


    @Override
    public List<Area> getAreaList() {

        String clazz = Thread.currentThread() .getStackTrace()[1].getClassName();
        String method = Thread.currentThread() .getStackTrace()[1].getMethodName();

        System.out.println(clazz +" "+ method);

        List<Area> areaList = areaDao.queryArea();

        ///
        String s = HttpKit.getHtmlContent("https://www.baidu.com/","getAreaList");
//        System.out.println("getHtmlContent()->"+s);
        ///

        this.areaDao.countLog(1);

        ///
//        test();
        ///


        return areaList;
    }

    private void test(){
        String s = HttpKit.getHtmlContent("https://vip.qidianla.com/","test");
//        System.out.println("getHtmlContent()->"+s);
    }

    private void test2(){
        String s = HttpKit.getHtmlContent("https://vip.qidianla.com/","test");
        System.out.println("getHtmlContent()->"+s);
    }

}
