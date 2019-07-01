package com.pojo;

/**
 * 用于发布签到任务数据返回
 */
public class TaskPackage {


    private Long taskId;
    private Integer CheckInCode;
    private String QrCodeSrc;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getCheckInCode() {
        return CheckInCode;
    }

    public void setCheckInCode(Integer checkInCode) {
        CheckInCode = checkInCode;
    }

    public String getQrCodeSrc() {
        return QrCodeSrc;
    }

    public void setQrCodeSrc(String qrCodeSrc) {
        QrCodeSrc = qrCodeSrc;
    }
}
