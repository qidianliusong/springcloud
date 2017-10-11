package com.jiuchunjiaoyu.micro.wzb.schoolBack.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 *缴费
 */
public class FeePayDTO {
	
	public static final int STATUS_NOT_PAY = 0;//未支付
	public static final int STATUS_PRE_PAY = 1;//等待支付
	public static final int STATUS_PAY = 2;//已支付
	public static final int STATUS_ERR = 3;//支付异常

	private int version;

	private Long feePayId;
	/**
	 * 班费详情id
	 */
	private Long feeDetailId;
	/**
	 * 小孩id
	 */
	private Long childId;
	/**
	 * 留言
	 */
	@ApiModelProperty("留言")
	private String message;
	/**
	 * 缴费人id
	 */
	private Long userId;
	private Date payTime;
	/**
	 * 支付金额
	 */
	@ApiModelProperty(value = "支付金额",readOnly = true)
	private BigDecimal amount;
	/**
	 * 支付状态，0为未支付，1为已支付
	 */
	@ApiModelProperty("状态，2为已支付,其他情形为未支付")
	private Integer status;
	
	private String userName;
	private String childName;
	
	private String outTradeNo;
	/**
	 * 小孩(学生)头像
	 */
	@ApiModelProperty(value = "孩子头像",readOnly = true)
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
