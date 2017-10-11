package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OptimisticLocking;

/**
 *代缴 
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class FeeAgent implements Serializable{

    public static final int STATUS_NOT_PAY = 0;
	public static final int STATUS_PRE_PAY = 1;//等待支付
	public static final int STATUS_PAY = 2;//支付成功
	public static final int STATUS_ERR = 3;//支付异常

	private static final long serialVersionUID = -6693055814079217862L;

	@Version
	private int version;

	@Id
	@GeneratedValue
	private Long feeAgentId;
	@Column
	@NotNull
	private Long userId;
	@Column
	private String userName;
	/**
	 * 收费详情id
	 */
	@Column(name="fee_detail_id")
	@NotNull
	private Long feeDetailId;
	/**
	 * 代缴孩子数
	 */
	@Column
	@NotNull
	private int childCount;
	/**
	 * 代缴总额
	 */
	@Column
	@NotNull
	@Min(0)
	private BigDecimal totalAmount;
	/**
	 * 代缴简介
	 */
	@Column
	private String agentDesc;
	/**
	 * 支付时间
	 */
	@Column
	private Date payTime;
	@Column
	private int status;
	
	@NotNull
	@Size(min=1)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY, mappedBy = "feeAgentId")
	private Set<FeeAgentChild> feeAgentChilds = new HashSet<>();
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fee_detail_id",insertable=false,updatable=false)
	private FeeDetail feeDetail;
	
	@Column
	private String outTradeNo;
	
	public Long getFeeAgentId() {
		return feeAgentId;
	}
	public void setFeeAgentId(Long feeAgentId) {
		this.feeAgentId = feeAgentId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getFeeDetailId() {
		return feeDetailId;
	}
	public void setFeeDetailId(Long feeDetailId) {
		this.feeDetailId = feeDetailId;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getAgentDesc() {
		return agentDesc;
	}
	public void setAgentDesc(String agentDesc) {
		this.agentDesc = agentDesc;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Set<FeeAgentChild> getFeeAgentChilds() {
		return feeAgentChilds;
	}
	public void setFeeAgentChilds(Set<FeeAgentChild> feeAgentChilds) {
		this.feeAgentChilds = feeAgentChilds;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
