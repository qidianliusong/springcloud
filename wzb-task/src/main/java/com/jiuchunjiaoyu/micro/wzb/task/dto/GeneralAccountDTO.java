package com.jiuchunjiaoyu.micro.wzb.task.dto;

import java.math.BigDecimal;

/**
 * 总账
 */
public class GeneralAccountDTO {
    private Long generalAccountId;
    private int version;
    private BigDecimal amount;
    private BigDecimal schoolAmount;

    public Long getGeneralAccountId() {
        return generalAccountId;
    }

    public void setGeneralAccountId(Long generalAccountId) {
        this.generalAccountId = generalAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSchoolAmount() {
        return schoolAmount;
    }

    public void setSchoolAmount(BigDecimal schoolAmount) {
        this.schoolAmount = schoolAmount;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
