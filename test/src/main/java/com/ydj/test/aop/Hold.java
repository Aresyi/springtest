package com.ydj.test.aop;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 4:45 下午
 * @description :
 */
public class Hold {

    private static final ThreadLocal<String> reqTL = new ThreadLocal<>();

    public static void put(String uuid){
        reqTL.set(uuid);
    }

    public static String get(){
        return reqTL.get();
    }

    public static void clean(){
        reqTL.remove();
    }
}
