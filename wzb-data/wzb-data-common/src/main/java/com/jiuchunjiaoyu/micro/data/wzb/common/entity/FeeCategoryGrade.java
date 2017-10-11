package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班费类别年级关系表
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
public class FeeCategoryGrade {

	@Id
	@GeneratedValue
	private Long id;
	private Long gradeId;
	private String gradeName;

	@Column(name="fee_category_id",updatable=false,insertable=false)
	private Long feeCategoryId;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fee_category_id")
	private FeeCategory feeCategory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public FeeCategory getFeeCategory() {
		return feeCategory;
	}

	public void setFeeCategory(FeeCategory feeCategory) {
		this.feeCategory = feeCategory;
	}

	public Long getFeeCategoryId() {
		return feeCategoryId;
	}

	public void setFeeCategoryId(Long feeCategoryId) {
		this.feeCategoryId = feeCategoryId;
	}
}
