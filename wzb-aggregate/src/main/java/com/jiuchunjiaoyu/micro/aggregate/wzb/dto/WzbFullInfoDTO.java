package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 微智宝全量信息
 */
public class WzbFullInfoDTO {

    private Long classId;
    @ApiModelProperty("年级名称")
    private String gradeName;

    private String className;
    @ApiModelProperty("班费总额")
    private BigDecimal amount;//班费总额
    @ApiModelProperty("已用金额")
    private BigDecimal usedAmount;//已用金额
    @ApiModelProperty("记账金额总计")
    private BigDecimal accountingAmount;//记账金额总计
    private int studentCount;//学生人数
    @ApiModelProperty("班委会成员列表")
    private List<UserDTO> committeeUsers;//班委会成员列表

    @ApiModelProperty("学校id")
    private Long schoolId;
    @ApiModelProperty("当前登录用户角色,1为班主任，2为老师，3为家长，4为家委会成员")
    private Integer roleEnum;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getAccountingAmount() {
        return accountingAmount;
    }

    public void setAccountingAmount(BigDecimal accountingAmount) {
        this.accountingAmount = accountingAmount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public List<UserDTO> getCommitteeUsers() {
        return committeeUsers;
    }

    public void setCommitteeUsers(List<UserDTO> committeeUsers) {
        this.committeeUsers = committeeUsers;
    }

    public Integer getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(Integer roleEnum) {
        this.roleEnum = roleEnum;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
