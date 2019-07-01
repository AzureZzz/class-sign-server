package com.pojo;

public class Task {
    private Long taskId;

    private Byte taskType;

    private String taskStart;

    private String taskEnd;

    private String taskCode;

    private Double taskLongitude;

    private Double taskLatitude;

    private Long classId;

    private String token;

    private String taskName;

    private String taskQrSrc;


    public String getTaskQrSrc() {
        return taskQrSrc;
    }

    public void setTaskQrSrc(String taskQrSrc) {
        this.taskQrSrc = taskQrSrc;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Byte getTaskType() {
        return taskType;
    }

    public void setTaskType(Byte taskType) {
        this.taskType = taskType;
    }

    public String getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(String taskStart) {
        this.taskStart = taskStart;
    }

    public String getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(String taskEnd) {
        this.taskEnd = taskEnd;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }

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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}