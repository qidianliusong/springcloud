package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级账户
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class SchoolDrawRecord {

    /**
     * 初始化状态（等待审批）
     */
    public static final int STATUS_INIT = 0;
    /**
     * 被驳回
     */
    public static final int STATUS_REJECT = 1;
    /**
     * 审核通过
     */
    public static final int STATUS_PASS = 2;

    @Id
    @GeneratedValue
    private Long schoolDrawRecordId;
    @NotNull
    private Long schoolId;
    private String schoolName;
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;
    private BigDecimal leftAmount;
    private String drawDesc;
    private Date drawTime;
    private Integer status;
    private Long applicantUserId;
    private String applicantUserName;
    private String applicantUserBankcard;//申请人银行卡
    private String applicantUserPhone;//申请人电话
    private Long auditorUserId;
    private String auditorUserName;
    @Column(updatable = false)
    private Date createTime;
    private String rejectReason;
    private Date rejectTime;

    public Long getSchoolDrawRecordId() {
        return schoolDrawRecordId;
    }

    public void setSchoolDrawRecordId(Long schoolDrawRecordId) {
        this.schoolDrawRecordId = schoolDrawRecordId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public String getDrawDesc() {
        return drawDesc;
    }

    public void setDrawDesc(String drawDesc) {
        this.drawDesc = drawDesc;
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getApplicantUserId() {
        return applicantUserId;
    }

    public void setApplicantUserId(Long applicantUserId) {
        this.applicantUserId = applicantUserId;
    }

    public String getApplicantUserName() {
        return applicantUserName;
    }

    public void setApplicantUserName(String applicantUserName) {
        this.applicantUserName = applicantUserName;
    }

    public Long getAuditorUserId() {
        return auditorUserId;
    }

    public void setAuditorUserId(Long auditorUserId) {
        this.auditorUserId = auditorUserId;
    }

    public String getAuditorUserName() {
        return auditorUserName;
    }

    public void setAuditorUserName(String auditorUserName) {
        this.auditorUserName = auditorUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Date getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    public String getApplicantUserPhone() {
        return applicantUserPhone;
    }

    public void setApplicantUserPhone(String applicantUserPhone) {
        this.applicantUserPhone = applicantUserPhone;
    }

    public String getApplicantUserBankcard() {
        return applicantUserBankcard;
    }

    public void setApplicantUserBankcard(String applicantUserBankcard) {
        this.applicantUserBankcard = applicantUserBankcard;
    }
}
