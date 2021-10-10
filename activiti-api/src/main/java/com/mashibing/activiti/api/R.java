package com.mashibing.activiti.api;

import java.util.HashMap;

/**
 * 返回数据
 * 
 * @author 孙志强

 * @date 2016年10月27日 下午9:59:27
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
