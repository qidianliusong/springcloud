package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 学校流水账户
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class SchoolFlowAccount {
    @Id
    @GeneratedValue
    private Long schoolFlowAccountId;
    private Long schoolId;
    private String schoolName;
    private String province;
    private String city;
    private String county;
    private String town;
    private BigDecimal amount;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSchoolFlowAccountId() {

        return schoolFlowAccountId;
    }

    public void setSchoolFlowAccountId(Long schoolFlowAccountId) {
        this.schoolFlowAccountId = schoolFlowAccountId;
    }
}
