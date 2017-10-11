package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 班费类别
 */
public class FeeCategoryDTO {
	/**
	 * 普通类型
	 */
	public static final int TYPE_COMMON = 0;
	/**
	 * 后台配置类型
	 */
	public static final int TYPE_BACKGROUND = 1;

	private Long feeCategoryId;
	
	private String feeCategoryName;
	private String feeCategoryDesc;
	private Long schoolId;
	private Long classId;
	/**
	 * 收费金额
	 */
	private BigDecimal amount;
	/**
	 * 0为普通类型,1为学校后台配置固定项目
	 */
	@ApiModelProperty("0为普通类型，1为学校后台配置固定项目")
	private Integer type;
	/**
	 * 状态:0为正常状态
	 */
	private Integer status;
	private Date createDate;
	/**
	 * 创建人id
	 */
	private Long createUserId;
	/**
	 * 最近一次修改时间
	 */
	private Date updateDate;
	/**
	 * 最近一次修改的修改人
	 */
	private Long updateUserId;
	
	public Long getFeeCategoryId() {
		return feeCategoryId;
	}
	public void setFeeCategoryId(Long feeCategoryId) {
		this.feeCategoryId = feeCategoryId;
	}
	public String getFeeCategoryName() {
		return feeCategoryName;
	}
	public void setFeeCategoryName(String feeCategoryName) {
		this.feeCategoryName = feeCategoryName;
	}
	public String getFeeCategoryDesc() {
		return feeCategoryDesc;
	}
	public void setFeeCategoryDesc(String feeCategoryDesc) {
		this.feeCategoryDesc = feeCategoryDesc;
	}
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	
}
