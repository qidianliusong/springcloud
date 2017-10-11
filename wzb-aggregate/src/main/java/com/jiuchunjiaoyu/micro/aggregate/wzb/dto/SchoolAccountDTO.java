package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.math.BigDecimal;

/**
 * 班级账户
 */
public class SchoolAccountDTO {

    private int version;

    private Long schoolAccountId;

    private Long schoolId;

    private String schoolName;

    private BigDecimal schoolAmount;

    private String province;
    private String city;//市
    private String county;//县
    private String town;//乡镇

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

    public Long getSchoolAccountId() {
        return schoolAccountId;
    }

    public void setSchoolAccountId(Long schoolAccountId) {
        this.schoolAccountId = schoolAccountId;
    }

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
