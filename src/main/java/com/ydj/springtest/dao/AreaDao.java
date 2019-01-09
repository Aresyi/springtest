package com.ydj.springtest.dao;

import com.ydj.springtest.entity.Area;

import java.util.List;

public interface AreaDao {

    /**
     * 列出地域列表
     *
     * @return
     */
    List<Area> queryArea();

    int countLog(int state);


}
