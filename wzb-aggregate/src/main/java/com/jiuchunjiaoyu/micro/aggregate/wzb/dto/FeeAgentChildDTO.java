package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 代缴孩子
 */
@ApiModel(value = "fee_agent_child", description = "代缴孩子")
public class FeeAgentChildDTO implements Serializable{

	private static final long serialVersionUID = 4262462255708945623L;
	
	private Long feeAgentChildId;
	@ApiModelProperty(value = "小孩名字")
	private String childName;
	private Long feeAgentId;
	
	public Long getFeeAgentChildId() {
		return feeAgentChildId;
	}
	public void setFeeAgentChildId(Long feeAgentChildId) {
		this.feeAgentChildId = feeAgentChildId;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public Long getFeeAgentId() {
		return feeAgentId;
	}
	public void setFeeAgentId(Long feeAgentId) {
		this.feeAgentId = feeAgentId;
	}
	
}
