package com.boray.entity;

public class Users {
    private String username;//Ô±¹¤¹¤ºÅ
    private String userpassword;//ÃÜÂë
    private String login;//ÕËºÅ
    private String id;
    private Integer loginstatus;//µÇÂ¼×´Ì¬

	private String usercode;//ÓÃ»§±àºÅ
	private Integer code;//µÇÂ¼×´Ì¬

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLoginstatus(Integer loginstatus) {
		this.loginstatus = loginstatus;
	}

	public String getUsername() {
		return username;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public String getLogin() {
		return login;
	}

	public String getId() {
		return id;
	}

	public Integer getLoginstatus() {
		return loginstatus;
	}
}
