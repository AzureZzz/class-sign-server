package com.pojo;

/**
 * 包装类
 */
public class MyClassPackage {

    private Long classId;

    private String classCode; //加课码

    private String className;  // 课程名称

    private String classQrcodeSrc;

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
        this.classCode = classCode;
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
