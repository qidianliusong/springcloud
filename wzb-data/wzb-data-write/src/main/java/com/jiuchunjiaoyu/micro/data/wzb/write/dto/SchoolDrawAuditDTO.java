package com.jiuchunjiaoyu.micro.data.wzb.write.dto;

import javax.validation.constraints.NotNull;

/**
 * 用于学校取款审核通过或驳回字段
 */
public class SchoolDrawAuditDTO {

    @NotNull
    private long schoolDrawRecordId;
    @NotNull
    private int status;
    @NotNull
    private long auditorUserId;
    @NotNull
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
