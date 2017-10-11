package com.jiuchunjiaoyu.micro.wzbOrder.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CompanyPayOrder {

    public static int STATUS_NOT_PAY = 0;
    public static int STATUS_PAY = 1;
    public static int STATUS_PAY_FAIL = 2;

    @Id
    @GeneratedValue
    private Long commpanyPayOrderId;
    @Column
    private String mchAppid;
    @Column
    private String mchid;
    @Column
    private String partnerTradeNo;
    @Column
    private String openid;
    @Column
    private String reUserName;
    @Column
    private Integer amount;
    @Column(name = "desc1")
    private String desc;
    @Column
    private String spbillCreateIp;
    @Column
    private String paymentNo;
    @Column
    private Date paymentTime;
    @Column
    private Integer status;
    @Column
    private String message;

    public Long getCommpanyPayOrderId() {
        return commpanyPayOrderId;
    }

    public void setCommpanyPayOrderId(Long commpanyPayOrderId) {
        this.commpanyPayOrderId = commpanyPayOrderId;
    }

    public String getMchAppid() {
        return mchAppid;
    }

    public void setMchAppid(String mchAppid) {
        this.mchAppid = mchAppid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getReUserName() {
        return reUserName;
    }

    public void setReUserName(String reUserName) {
        this.reUserName = reUserName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
