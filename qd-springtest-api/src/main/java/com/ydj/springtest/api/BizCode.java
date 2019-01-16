package com.ydj.springtest.api;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BizCode implements Serializable{

	/**业务编码*/
	private int code;

	/**具体描述*/
	private String msg;

	public BizCode() {
	}

	public BizCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public final static BizCode success = new BizCode(200,"成功");
	public final static BizCode fail = new BizCode(500,"失败");


}
