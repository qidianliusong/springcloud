package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 总账
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class GeneralAccount {
    @Id
    @GeneratedValue
    private Long generalAccountId;
    @Version
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
