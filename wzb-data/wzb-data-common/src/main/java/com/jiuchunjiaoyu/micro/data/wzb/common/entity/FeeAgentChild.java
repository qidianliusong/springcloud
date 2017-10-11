package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 代缴孩子
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class FeeAgentChild implements Serializable{

	private static final long serialVersionUID = 4262462255708945623L;
	@Id
	@GeneratedValue
	private Long feeAgentChildId;
	@Column
	private String childName;
	@Column(name="fee_agent_id",updatable=false,insertable=false)
	private Long feeAgentId;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fee_agent_id")
	private FeeAgent feeAgent;
	
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
	public FeeAgent getFeeAgent() {
		return feeAgent;
	}
	public void setFeeAgent(FeeAgent feeAgent) {
		this.feeAgent = feeAgent;
	}
	
}
