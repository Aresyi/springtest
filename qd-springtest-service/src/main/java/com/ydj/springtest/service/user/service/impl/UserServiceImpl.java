package com.ydj.springtest.service.user.service.impl;

import com.ydj.springtest.service.user.dao.UserInfoDao;
import com.ydj.springtest.service.user.dao.entity.UserInfoEntity;
import com.ydj.springtest.service.user.service.UserService;
import com.ydj.springtest.utils.HttpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Resource(name = "catJdbcTemplateTest")
    private JdbcTemplate catJdbcTemplateTest;


    @Override
    public List<UserInfoEntity> getUserList() {

        String clazz = Thread.currentThread() .getStackTrace()[1].getClassName();
        String method = Thread.currentThread() .getStackTrace()[1].getMethodName();
        System.out.println(clazz +" "+ method);

        List<UserInfoEntity> areaList = userInfoDao.getUserInfo();


        test("https://vip.qidianla.com/");


        this.userInfoDao.countLog(1);


//       test("https://www.baidu.com/");

        return areaList;
    }

    @Override
    public void testSpringjdbc() {
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
    }

    private void test(String url){
        String s = HttpKit.getHtmlContent(url,"test");
//        System.out.println("getHtmlContent()->"+s);
    }


}
