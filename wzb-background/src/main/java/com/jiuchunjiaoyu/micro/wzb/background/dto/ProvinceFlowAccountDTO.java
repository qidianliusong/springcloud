package com.jiuchunjiaoyu.micro.wzb.background.dto;

import java.math.BigDecimal;

/**
 * 总账
 */
public class ProvinceFlowAccountDTO {
    private Long provinceFlowAccountId;
    private BigDecimal amount;
    private String provinceName;

    public Long getProvinceFlowAccountId() {
        return provinceFlowAccountId;
    }

    public void setProvinceFlowAccountId(Long provinceFlowAccountId) {
        this.provinceFlowAccountId = provinceFlowAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
