package com.pojo;

public class Sign {
    private Long userId;

    private Long taskId;

    private Byte signState;   //签到状态  0 未签到  1 出勤  2 异常

    private String signTime;  //签到时间

    private String token;

    private Long classId;  //额外增加课堂id号

    private String code; //额外增加签到码字段

    private Integer locationType; // 额外增加定位状态： 1 在范围内（正常定位）    2 定位异常

    private String userName;  //学生姓名

    private Byte signType;  // 额外增加签到类型

    private String taskName;   //考勤名称

    private Double taskLongitude;

    private Double taskLatitude;

    public Double getTaskLongitude() {
        return taskLongitude;
    }

    public void setTaskLongitude(Double taskLongitude) {
        this.taskLongitude = taskLongitude;
    }

    public Double getTaskLatitude() {
        return taskLatitude;
    }

    public void setTaskLatitude(Double taskLatitude) {
        this.taskLatitude = taskLatitude;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Byte getSignType() {
        return signType;
    }

    public void setSignType(Byte signType) {
        this.signType = signType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Byte getSignState() {
        return signState;
    }

    public void setSignState(Byte signState) {
        this.signState = signState;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}