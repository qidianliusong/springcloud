package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 收费详情
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class FeeDetail implements Serializable{

	private static final long serialVersionUID = 1132530621582704703L;

	public static final int STATUS_OPEN = 0;//开启状态
	public static final int STATUS_CLOSE = 1;//关闭状态
	public static final int STATUS_DEL = 2;//删除状态

	@Id
	@GeneratedValue
	@NotNull(groups = { Update.class })
	private Long feeDetailId;
	@Column(name="class_id")
	@NotNull
	private Long classId;
	@Column
	private String className;

	@Column
	private Long schoolId;
	@Column
	private Long releaseUserId;
	@Column
	@NotNull
	private String feeCategoryName;
	@Column(name = "fee_category_id")
	private Long feeCategoryId;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fee_category_id",insertable=false,updatable=false)
	private FeeCategory feeCategory;

	@Column
	private String releaseUserName;
	@Column
	private String feeDetailName;
	@Column
	@Length(min=4,max=5000)
	private String feeDetailDesc;
	@Column
	@NotNull
	private BigDecimal amount;
	@Column
	private BigDecimal totalAmount;
	@Column(updatable=false)
	private Date createTime;
	@Column
	private Date updateTime;
	@Column(updatable=false)
	private Integer studentCount;
	@Column
	private Integer paidCount = 0;
	@Column
	private Integer status;
	@Column
	private String releaseUserImg;//发布人头像

	//不入库字段
	@Transient
	private List<FeePay> FeePays;
    @Transient
	private Boolean paid = false;//是否已缴费
	
	@Version
	private int version;

	public Long getFeeDetailId() {
		return feeDetailId;
	}

	public void setFeeDetailId(Long feeDetailId) {
		this.feeDetailId = feeDetailId;
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

	public Long getReleaseUserId() {
		return releaseUserId;
	}

	public void setReleaseUserId(Long releaseUserId) {
		this.releaseUserId = releaseUserId;
	}

	public String getFeeCategoryName() {
		return feeCategoryName;
	}

	public void setFeeCategoryName(String feeCategoryName) {
		this.feeCategoryName = feeCategoryName;
	}

	public Long getFeeCategoryId() {
		return feeCategoryId;
	}

	public void setFeeCategoryId(Long feeCategoryId) {
		this.feeCategoryId = feeCategoryId;
	}

	public String getReleaseUserName() {
		return releaseUserName;
	}

	public void setReleaseUserName(String releaseUserName) {
		this.releaseUserName = releaseUserName;
	}

	public String getFeeDetailName() {
		return feeDetailName;
	}

	public void setFeeDetailName(String feeDetailName) {
		this.feeDetailName = feeDetailName;
	}

	public String getFeeDetailDesc() {
		return feeDetailDesc;
	}

	public void setFeeDetailDesc(String feeDetailDesc) {
		this.feeDetailDesc = feeDetailDesc;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public Integer getPaidCount() {
		return paidCount;
	}

	public void setPaidCount(Integer paidCount) {
		this.paidCount = paidCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<FeePay> getFeePays() {
		return FeePays;
	}

	public void setFeePays(List<FeePay> feePays) {
		FeePays = feePays;
	}

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

	public String getReleaseUserImg() {
		return releaseUserImg;
	}

	public void setReleaseUserImg(String releaseUserImg) {
		this.releaseUserImg = releaseUserImg;
	}

    public FeeCategory getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(FeeCategory feeCategory) {
        this.feeCategory = feeCategory;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public static interface Update {

	}
}
