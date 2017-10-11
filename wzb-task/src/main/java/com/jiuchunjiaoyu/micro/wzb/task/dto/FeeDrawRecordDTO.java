package com.jiuchunjiaoyu.micro.wzb.task.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 班费支取
 */
public class FeeDrawRecordDTO {

	public static int STATUS_NOT_PAY = 0;//未支付，初始状态
    public static int STATUS_PAY = 1;//支付完成
    public static int STATUS_PAY_FAIL = 2;//支付失败

	private Long feeDrawRecordId;
	private Long userId;
	private String userName;
	/**
	 * 取款金额
	 */
	private BigDecimal amount;
	private String reason;
	private Long classId;
	private Long schoolId;
	/**
	 * 状态，0:未支付，1:支付完成，2:支付失败
	 */
	private Integer status;
	private Long auditorId;
	private String auditorName;
	private Date createTime;
	private Date auditorTime;
	private Date drawTime;
	private String partnerTradeNo;

	public Long getFeeDrawRecordId() {
		return feeDrawRecordId;
	}
	public void setFeeDrawRecordId(Long feeDrawRecordId) {
		this.feeDrawRecordId = feeDrawRecordId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getAuditorTime() {
		return auditorTime;
	}
	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}
	public Date getDrawTime() {
		return drawTime;
	}
	public void setDrawTime(Date drawTime) {
		this.drawTime = drawTime;
	}

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }
}
