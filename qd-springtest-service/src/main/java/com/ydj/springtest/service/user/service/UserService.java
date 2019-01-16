package com.ydj.springtest.service.user.service;

import com.ydj.springtest.service.user.dao.entity.UserInfoEntity;

import java.util.List;

public interface UserService {

    List<UserInfoEntity> getUserList() ;

    void testSpringjdbc();
}