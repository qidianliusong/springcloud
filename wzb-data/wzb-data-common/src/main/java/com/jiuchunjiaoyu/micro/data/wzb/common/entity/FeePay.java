package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OptimisticLocking;

/**
 *缴费
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class FeePay {
	
	public static final int STATUS_NOT_PAY = 0;//未支付
	public static final int STATUS_PRE_PAY = 1;//等待支付
	public static final int STATUS_PAY = 2;//已支付
	public static final int STATUS_ERR = 3;//支付异常

	@Version
	private int version;

	@Id
	@GeneratedValue
	private Long feePayId;
	/**
	 * 班费详情id
	 */
	@Column(name="fee_detail_id")
	private Long feeDetailId;
	/**
	 * 小孩id
	 */
	@Column
	private Long childId;
	/**
	 * 留言
	 */
	@Column
	private String message;
	/**
	 * 缴费人id
	 */
	@Column
	private Long userId;
	@Column
	private Date payTime;
	/**
	 * 支付金额
	 */
	@Column
	private BigDecimal amount;
	/**
	 * 支付状态，0为未支付，1为等待支付，2为已支付，3为支付异常
	 */
	@Column
	private Integer status;
	
	@Column
	private String userName;
	@Column
	private String childName;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fee_detail_id",insertable=false,updatable=false)
	private FeeDetail feeDetail;
	@Column
	private String outTradeNo;
	@Column
	private String avatarUrl;
	
	public Long getFeePayId() {
		return feePayId;
	}
	public void setFeePayId(Long feePayId) {
		this.feePayId = feePayId;
	}
	public Long getFeeDetailId() {
		return feeDetailId;
	}
	public void setFeeDetailId(Long feeDetailId) {
		this.feeDetailId = feeDetailId;
	}
	public Long getChildId() {
		return childId;
	}
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public FeeDetail getFeeDetail() {
		return feeDetail;
	}
	public void setFeeDetail(FeeDetail feeDetail) {
		this.feeDetail = feeDetail;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
