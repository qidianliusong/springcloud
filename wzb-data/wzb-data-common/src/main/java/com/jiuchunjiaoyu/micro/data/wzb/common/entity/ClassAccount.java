package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;
/**
 * 班级账户
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "class_account")
@OptimisticLocking
public class ClassAccount implements Serializable{

	private static final long serialVersionUID = 5271868289693104061L;
	@Id
	@GeneratedValue
	@Column(name="class_account_id")
	private Long classAccountId;
	@Column(name="class_id",nullable=false,updatable=false,unique = true)
	private Long classId;
	@Column(name="schoolId")
	private Long schoolId;
	@Column
	private String className;
	@Column
	private String schoolName;
	@Column
	private Long gradeId;
	@Column
	private String gradeName;
	/**
	 * 班费总额
	 */
	@Column
	private BigDecimal amount;
	/**
	 * 已用金额
	 */
	@Column
	private BigDecimal usedAmount;
	/**
	 * 记账金额总计
	 */
	@Column
	private BigDecimal accountingAmount;
	@Version
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
