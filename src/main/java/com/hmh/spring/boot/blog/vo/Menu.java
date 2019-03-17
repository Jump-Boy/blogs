package com.hmh.spring.boot.blog.vo;

import java.io.Serializable;

/**
 * admins后台管理页面菜单值对象.
 *
 * @author hmh
 * @date 2019/3/6
 */
public class Menu implements Serializable{
 
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单类目名称
	 */
	private String name;
	/**
	 * 菜单类目地址
	 */
	private String url;
	
	public Menu(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
