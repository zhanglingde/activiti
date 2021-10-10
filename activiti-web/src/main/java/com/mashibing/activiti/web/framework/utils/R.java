package com.mashibing.activiti.web.framework.utils;

import java.util.HashMap;

/**
 * 返回数据
 * 
 * @author 孙志强

 * @date 2019年12月19日 下午4:59:27
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 200);
	}
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String info) {
		return error(500, info);
	}
	
	public static R error(int code, String info) {
		R r = new R();
		r.put("code", code);
		r.put("info", info);
		return r;
	}
	public static R ok(String info) {
		R r = new R();
		r.put("info", info);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
