package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 预支付数据
 */
public class PrePayDTO {

    private String prepayId;

    private String mwebUrl;

    private String errCode;

    private String errCodeDes;

    private String paySign;

    private String timeStamp;

    private String appid;

    private Long feePayId;//缴费id或者代缴id

    @JsonIgnore
    private WzbOrderDTO wzbOrderDTO;

    private String nonceStr;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getMwebUrl() {
        return mwebUrl;
    }

    public void setMwebUrl(String mwebUrl) {
        this.mwebUrl = mwebUrl;
    }

    public WzbOrderDTO getWzbOrderDTO() {
        return wzbOrderDTO;
    }

    public void setWzbOrderDTO(WzbOrderDTO wzbOrderDTO) {
        this.wzbOrderDTO = wzbOrderDTO;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getFeePayId() {
        return feePayId;
    }

    public void setFeePayId(Long feePayId) {
        this.feePayId = feePayId;
    }
}
