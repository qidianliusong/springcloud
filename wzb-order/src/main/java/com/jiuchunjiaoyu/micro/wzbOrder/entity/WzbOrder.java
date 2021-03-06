package com.jiuchunjiaoyu.micro.wzbOrder.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class WzbOrder {

    public static final int STATUS_PRE_PAY = 0;
    public static final int STATUS_PAY = 1;
    public static final int STATUS_EXPIRE = 2;
    public static final int STATUS_FAIL = 3;

    public static final int TYPE_CLASS_FEE_PAY = 1;//班费缴纳
    public static final int TYPE_CLASS_FEE_AGENT = 2;//班费代缴

    @Id
    @GeneratedValue
    private Long orderId;
    @Column
    private Integer status;
    @Column(updatable = false)
    private Date createTime;
    @Column(updatable = false)
    private Date expireTime;
    @Column
    private Date payTime;
    @Column
    private String appid;
    @Column
    private String mchId;
    @Column
    private String deviceInfo;
    @Column
    private String body;
    @Column
    private String detail;
    @Column
    private String attach;
    @Column
    private String outTradeNo;
    @Column
    private String feeType;
    @Column
    private Integer totalFee;
    @Column
    private String spbillCreateIp;
    @Column
    private String goodsTag;
    @Column
    private String tradeType;
    @Column
    private String limitPay;
    @Column
    private String openid;
    @Column
    private String transactionId;
    @Column
    private String message;
    @Column
    private Integer wzbOrderType;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getWzbOrderType() {
        return wzbOrderType;
    }

    public void setWzbOrderType(Integer wzbOrderType) {
        this.wzbOrderType = wzbOrderType;
    }
}
