package com.jiuchunjiaoyu.micro.wzb.background.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 用于学校取款审核通过或驳回字段
 */
public class SchoolDrawAuditDTO {

    @NotNull
    @ApiModelProperty(value = "取款申请id", required = true)
    private long schoolDrawRecordId;
    @NotNull
    @ApiModelProperty(value = "审批状态，1为被驳回，2为审核通过", required = true)
    private int status;
    @ApiModelProperty(value = "审批人id", readOnly = true)
    private long auditorUserId;
    @ApiModelProperty(value = "审批人姓名", readOnly = true)
    private String auditorUserName;
    private String message;

    public long getSchoolDrawRecordId() {
        return schoolDrawRecordId;
    }

    public void setSchoolDrawRecordId(long schoolDrawRecordId) {
        this.schoolDrawRecordId = schoolDrawRecordId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAuditorUserId() {
        return auditorUserId;
    }

    public void setAuditorUserId(long auditorUserId) {
        this.auditorUserId = auditorUserId;
    }

    public String getAuditorUserName() {
        return auditorUserName;
    }

    public void setAuditorUserName(String auditorUserName) {
        this.auditorUserName = auditorUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
