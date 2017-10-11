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
 * 班费使用记账
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class FeeTake {

	@Id
	@GeneratedValue
	private Long feeTakeId;
	/**
	 * 记账金额
	 */
	@Column
	private BigDecimal amount;
	/**
	 * 记账描述（资金用途）
	 */
	@Column
	private String feeTakeDesc;
	/**
	 * 班级账户id
	 */
	@Column
	private Long classId;
	/**
	 * 记账人id
	 */
	@Column
	private Long userId;
	/**
	 * 记账人名称
	 */
	@Column
	private String userName;
	@Column(updatable=false)
	private Date createTime;
	
	public Long getFeeTakeId() {
		return feeTakeId;
	}
	public void setFeeTakeId(Long feeTakeId) {
		this.feeTakeId = feeTakeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getFeeTakeDesc() {
		return feeTakeDesc;
	}
	public void setFeeTakeDesc(String feeTakeDesc) {
		this.feeTakeDesc = feeTakeDesc;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
