package com.jiuchunjiaoyu.micro.wzb.task.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 代缴
 */
public class FeeAgentDTO implements Serializable {

    private static final long serialVersionUID = -6693055814079217862L;

    public static final int STATUS_NOT_PAY = 0;
    public static final int STATUS_PRE_PAY = 1;//等待支付
    public static final int STATUS_PAY = 2;//支付成功
    public static final int STATUS_ERR = 3;//支付异常

    private int version;

    private Long feeAgentId;

    private Long userId;
    private String userName;
    /**
     * 收费详情id
     */
    private Long feeDetailId;
    /**
     * 代缴孩子数
     */
    private int childCount;
    /**
     * 代缴总额
     */
    private BigDecimal totalAmount;
    /**
     * 代缴简介
     */
    private String agentDesc;
    /**
     * 支付时间
     */
    private Date payTime;

    private int status;

    private Set<FeeAgentChildDTO> feeAgentChilds;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
