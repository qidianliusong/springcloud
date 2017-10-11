package com.jiuchunjiaoyu.micro.wzb.task.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 班级账户
 */
public class ClassAccountDTO implements Serializable {

    private static final long serialVersionUID = 5271868289693104061L;
    private Long classAccountId;
    private Long classId;
    private Long schoolId;
    private String className;
    private String schoolName;
    private Long gradeId;
    private String gradeName;
    /**
     * 班费总额
     */
    private BigDecimal amount;
    /**
     * 已用金额
     */
    private BigDecimal usedAmount;
    /**
     * 记账金额总计
     */
    private BigDecimal accountingAmount;
    private int version;

    public Long getClassAccountId() {
        return classAccountId;
    }

    public void setClassAccountId(Long classAccountId) {
        this.classAccountId = classAccountId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
