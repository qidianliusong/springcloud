package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.math.BigDecimal;

/**
 *  支付消息
 */
public class PayMessageDTO {

    public static final Integer TYPE_FEE_PAY = 1;//缴费
    public static final Integer TYPE_FEE_AGENT = 2;//代缴
    public static final Integer TYPE_DRAW = 3;//取款（企业支付）

    private Integer type;
    private BigDecimal amount;
    private Long id;
    private String tradeNo;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
