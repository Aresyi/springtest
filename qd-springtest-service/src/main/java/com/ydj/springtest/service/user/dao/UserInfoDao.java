package com.ydj.springtest.service.user.dao;

import com.ydj.springtest.service.user.dao.entity.UserInfoEntity;

import java.util.List;

public interface UserInfoDao {

    List<UserInfoEntity> getUserInfo();

    int countLog(int state);


}
