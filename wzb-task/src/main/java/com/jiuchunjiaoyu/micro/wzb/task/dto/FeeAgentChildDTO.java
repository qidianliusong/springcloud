package com.jiuchunjiaoyu.micro.wzb.task.dto;

import java.io.Serializable;

/**
 * 代缴孩子
 */
public class FeeAgentChildDTO implements Serializable {

    private static final long serialVersionUID = 4262462255708945623L;

    private Long feeAgentChildId;
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
