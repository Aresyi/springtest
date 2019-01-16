package com.ydj.springtest.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CommonResponse<T> implements Serializable {

    private T data;
    private BizCode bizCode = BizCode.success;

    public static CommonResponse success(){
        return success("SUCCESS");
    }

    public static CommonResponse fail(){
        CommonResponse response = new CommonResponse();
        response.setBizCode(BizCode.fail);
        response.setData("");
        return response;
    }


    public static <T> CommonResponse success(T data){
        CommonResponse response = new CommonResponse();
        response.setBizCode(BizCode.success);
        response.setData(data);
        return response;
    }

    public static  CommonResponse fail(int code,String msg){
        CommonResponse response = new CommonResponse();
        response.setBizCode(new BizCode(code,msg));
        response.setData("");
        return response;
    }


}
