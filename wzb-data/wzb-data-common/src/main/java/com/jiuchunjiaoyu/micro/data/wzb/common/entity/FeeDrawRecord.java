package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 班费支取
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class FeeDrawRecord {

	public static int STATUS_NOT_PAY = 0;//未支付，初始状态
    public static int STATUS_PAY = 1;//支付完成
    public static int STATUS_PAY_FAIL = 2;//支付失败

	@Id
	@GeneratedValue
	private Long feeDrawRecordId;
	@Column(updatable = false)
	private Long userId;
	@Column(updatable = false)
	private String userName;
	/**
	 * 取款金额
	 */
	@Column
	private BigDecimal amount;
	@Column
	private String reason;
	@Column
	private Long classId;
	@Column
	private Long schoolId;
	/**
	 * 状态，0:未支付，1:支付完成，2:支付失败
	 */
	@Column
	private Integer status;
	@Column
	private Long auditorId;
	@Column
	private String auditorName;
	@Column(updatable = false)
	private Date createTime;
	@Column
	private Date auditorTime;
	@Column
	private Date drawTime;
	@Column
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
