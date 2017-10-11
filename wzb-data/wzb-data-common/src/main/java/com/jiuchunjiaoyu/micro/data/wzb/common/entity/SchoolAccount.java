package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 班级账户
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table
@OptimisticLocking
public class SchoolAccount{

	@Version
	private int version;
	@Id
	@GeneratedValue
	@Column
	private Long schoolAccountId;
	@Column
	private Long schoolId;
	@Column
	private String schoolName;
	@Column
	private BigDecimal schoolAmount;
	@Column
	private String province;
	@Column
	private String city;//市
	@Column
	private String county;//县
	@Column
	private String town;//乡镇


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
}
