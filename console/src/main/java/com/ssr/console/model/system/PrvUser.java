package com.ssr.console.model.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ssr.base.util.json.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Table(name = "PRV_USER")
public class PrvUser implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 登陆名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 登陆密码
     */
    @Column(name = "login_password")
    private String loginPassword;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 账户状态
     */
    private String state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后登陆时间
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 最后登录IP
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 是否需要更换密码
     */
    @Column(name = "pwd_change")
    private String pwdChange;
    
    /**
     * 登录失败次数
     */
    @Column(name = "login_error_times")
    private Integer loginErrorTimes;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取登陆名
     *
     * @return login_name - 登陆名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登陆名
     *
     * @param loginName 登陆名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取登陆密码
     *
     * @return login_password - 登陆密码
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * 设置登陆密码
     *
     * @param loginPassword 登陆密码
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * 获取真实姓名
     *
     * @return name - 真实姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置真实姓名
     *
     * @param name 真实姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取账户状态
     *
     * @return state - 账户状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置账户状态
     *
     * @param state 账户状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后登陆时间
     *
     * @return login_time - 最后登陆时间
     */
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置最后登陆时间
     *
     * @param loginTime 最后登陆时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取最后登录IP
     *
     * @return login_ip - 最后登录IP
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置最后登录IP
     *
     * @param loginIp 最后登录IP
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取是否需要更换密码
     *
     * @return pwd_change - 是否需要更换密码
     */
    public String getPwdChange() {
        return pwdChange;
    }

    /**
     * 设置是否需要更换密码
     *
     * @param pwdChange 是否需要更换密码
     */
    public void setPwdChange(String pwdChange) {
        this.pwdChange = pwdChange;
    }

    /**
     * 获取登录失败次数
     *
     * @return login_error_times - 登录失败次数
     */
	public Integer getLoginErrorTimes() {
		return loginErrorTimes;
	}

	/**
     * 设置登录失败次数
     *
     * @return login_error_times - 登录失败次数
     */
	public void setLoginErrorTimes(Integer loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}
}