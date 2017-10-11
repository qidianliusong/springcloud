package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 代缴
 *
 */
@ApiModel(value = "fee_agent", description = "代缴对象")
public class FeeAgentDTO implements Serializable {

	private static final long serialVersionUID = -6693055814079217862L;
	
	public static final int STATUS_NOT_PAY = 0;
	public static final int STATUS_PRE_PAY = 1;//等待支付
	public static final int STATUS_PAY = 2;//支付成功
	public static final int STATUS_ERR = 3;//支付异常

	private int version;

	@ApiModelProperty(value = "ID")
	private Long feeAgentId;

	@ApiModelProperty(readOnly = true)
	private Long userId;
	@ApiModelProperty(readOnly = true,value = "缴费人名称")
	private String userName;
	/**
	 * 收费详情id
	 */
	@ApiModelProperty(value = "收费详情id",required = true)
	@NotNull
	private Long feeDetailId;
	/**
	 * 代缴孩子数
	 */
	@ApiModelProperty(value = "孩子数量")
	@NotNull
	private int childCount;
	/**
	 * 代缴总额
	 */
	@ApiModelProperty(value = "代缴总额")
	@NotNull
	@Min(0)
	@Digits(integer=10,fraction=2)
	private BigDecimal totalAmount;
	/**
	 * 代缴简介
	 */
	@ApiModelProperty(value = "代缴简介")
	private String agentDesc;
	/**
	 * 支付时间
	 */
	@ApiModelProperty(readOnly = true)
	private Date payTime;

	@ApiModelProperty(value = "支付状态，0为未支付，1为已支付",readOnly = true)
	private int status;

	@ApiModelProperty(value = "代缴孩子",required = false)
	@NotNull
	@Size(min = 1)
	private Set<FeeAgentChildDTO> feeAgentChilds;

	@ApiModelProperty(readOnly = true)
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

	public Set<FeeAgentChildDTO> getFeeAgentChilds() {
		return feeAgentChilds;
	}

	public void setFeeAgentChilds(Set<FeeAgentChildDTO> feeAgentChilds) {
		this.feeAgentChilds = feeAgentChilds;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
