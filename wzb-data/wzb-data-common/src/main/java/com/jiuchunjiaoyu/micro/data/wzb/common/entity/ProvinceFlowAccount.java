package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 总账
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class ProvinceFlowAccount {
    @Id
    @GeneratedValue
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
