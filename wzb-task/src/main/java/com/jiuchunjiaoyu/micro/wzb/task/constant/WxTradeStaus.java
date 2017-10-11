package com.jiuchunjiaoyu.micro.wzb.task.constant;

/**
 * 微信支付状态
 */
public enum WxTradeStaus {

    success("SUCCESS", "支付成功"),
    refund("REFUND", "转入退款"),

    notpay("NOTPAY", "未支付"),

    close("CLOSED", "已关闭"),
    revoked("REVOKED", "已撤销"),
    userpaying("USERPAYING", "用户支付中"),

    payerror("PAYERROR", "支付失败");

    private String code;
    private String messsage;

    private WxTradeStaus(String code, String messsage) {
        this.code = code;
        this.messsage = messsage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }
}
