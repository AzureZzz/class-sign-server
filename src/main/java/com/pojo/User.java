package com.pojo;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 10086;

    private Long userId;


    private String userPhone;

    private String userPassword;

    private String userName;

    //学号可以为空
    private String userNo;

    private String userSchool;

    private Byte userType;

    private Integer verifyCode; //验证码

    private String token; //token


    private String userHeadSrc;  //头像链接

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserHeadSrc() {
        return userHeadSrc;
    }

    public void setUserHeadSrc(String userHeadSrc) {
        this.userHeadSrc = userHeadSrc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(Integer verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool == null ? null : userSchool.trim();
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }
}