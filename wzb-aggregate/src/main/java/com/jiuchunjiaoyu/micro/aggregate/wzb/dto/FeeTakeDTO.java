package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * 班费使用记账
 *
 */
public class FeeTakeDTO {

	private Long feeTakeId;
	/**
	 * 记账金额
	 */
	@NotNull
	@Min(0)
	@Digits(integer=10,fraction=2)
    @ApiModelProperty(required = true,value = "记账金额")
	private BigDecimal amount;
	/**
	 * 记账描述（资金用途）
	 */
	@NotNull
	@Length(max=50,min=4)
	@ApiModelProperty(required = true,value = "描述")
	private String feeTakeDesc;
	/**
	 * 班级账户id
	 */
	@ApiModelProperty(required = true)
	@NotNull
	private Long classId;
	/**
	 * 记账人id
	 */
	private Long userId;
	/**
	 * 记账人名称
	 */
    @ApiModelProperty(readOnly = true)
	private String userName;
    @ApiModelProperty(readOnly = true,value = "创建时间")
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
}
