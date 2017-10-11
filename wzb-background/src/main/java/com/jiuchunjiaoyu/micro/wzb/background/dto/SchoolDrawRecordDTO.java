package com.jiuchunjiaoyu.micro.wzb.background.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级账户
 */
public class SchoolDrawRecordDTO {

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

    private Long schoolDrawRecordId;
    @NotNull
    @ApiModelProperty(value = "学校id", required = true)
    private Long schoolId;
    private String schoolName;
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 10, fraction = 2)
    @ApiModelProperty(value = "取款金额", required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "剩余金额(只读)", readOnly = true)
    private BigDecimal leftAmount;
    @ApiModelProperty(value = "取款描述")
    private String drawDesc;
    @ApiModelProperty(value = "取款时间",readOnly = true)
    private Date drawTime;
    @ApiModelProperty(value = "状态，0为待审，1为被驳回，2为审批通过",readOnly = true)
    private Integer status;

    @ApiModelProperty(value = "申请人id",required = true)
    private Long applicantUserId;
    @ApiModelProperty(value = "申请人名称",required = true)
    private String applicantUserName;
    @ApiModelProperty(value = "申请人银行卡",required = true)
    private String applicantUserBankcard;//申请人银行卡
    @ApiModelProperty(value = "申请人电话",required = true)
    private String applicantUserPhone;//申请人电话
    @ApiModelProperty(value="审批人id",readOnly = true)
    private Long auditorUserId;
    @ApiModelProperty(value="审批人名称",readOnly = true)
    private String auditorUserName;
    @ApiModelProperty(value="创建时间",readOnly = true)
    private Date createTime;
    @ApiModelProperty(value="驳回原因",readOnly = true)
    private String rejectReason;
    @ApiModelProperty(value="驳回时间",readOnly = true)
    private Date rejectTime;

    public static int getStatusInit() {
        return STATUS_INIT;
    }

    public static int getStatusReject() {
        return STATUS_REJECT;
    }

    public static int getStatusPass() {
        return STATUS_PASS;
    }

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

    public String getApplicantUserBankcard() {
        return applicantUserBankcard;
    }

    public void setApplicantUserBankcard(String applicantUserBankcard) {
        this.applicantUserBankcard = applicantUserBankcard;
    }

    public String getApplicantUserPhone() {
        return applicantUserPhone;
    }

    public void setApplicantUserPhone(String applicantUserPhone) {
        this.applicantUserPhone = applicantUserPhone;
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
}
