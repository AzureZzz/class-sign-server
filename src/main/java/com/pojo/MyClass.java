package com.pojo;

public class MyClass {
    private Long classId;

    private String classCode;

    private String className;

    private String classNo;

    private String classQrcode;

    private String classQrcodeSrc;

    private Long userId;


    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode == null ? null : classCode.trim();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo == null ? null : classNo.trim();
    }

    public String getClassQrcode() {
        return classQrcode;
    }

    public void setClassQrcode(String classQrcode) {
        this.classQrcode = classQrcode == null ? null : classQrcode.trim();
    }

    public String getClassQrcodeSrc() {
        return classQrcodeSrc;
    }

    public void setClassQrcodeSrc(String classQrcodeSrc) {
        this.classQrcodeSrc = classQrcodeSrc == null ? null : classQrcodeSrc.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}