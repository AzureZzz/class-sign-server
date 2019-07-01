package com.pojo;

/**
 * 我的课堂列表显示的信息
 */
public class MyClassInfo {

    private String userHeadSrc;  //老师头像

    private String userName;  //老师姓名

    private String classCode;  // 课堂加课码

    private Integer stuNumber;  //学生人数

    private String className; //课堂名称

    private String classQrcodeSrc; //加课二维码地址


    public String getUserHeadSrc() {
        return userHeadSrc;
    }

    public void setUserHeadSrc(String userHeadSrc) {
        this.userHeadSrc = userHeadSrc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(Integer stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassQrcodeSrc() {
        return classQrcodeSrc;
    }

    public void setClassQrcodeSrc(String classQrcodeSrc) {
        this.classQrcodeSrc = classQrcodeSrc;
    }
}
