package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 班级信息 
 *
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClassDTO {

	private Long id;
	private String name;
	private Long schoolId;
	private Long masterId;
	private Long gradeId;
	private String code;
	private Integer teacherCount;
	private Integer parentCount;
	private Integer studentCount;
	private Integer isJoin;
	private Integer groupId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	
	private UserDTO master;
	private GradeDTO grade;
	private SchoolDTO school;
	
	public UserDTO getMaster() {
		return master;
	}
	public void setMaster(UserDTO master) {
		this.master = master;
	}
	public GradeDTO getGrade() {
		return grade;
	}
	public void setGrade(GradeDTO grade) {
		this.grade = grade;
	}
	public SchoolDTO getSchool() {
		return school;
	}
	public void setSchool(SchoolDTO school) {
		this.school = school;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public Long getMasterId() {
		return masterId;
	}
	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}
	public Long getGradeId() {
		return gradeId;
	}
	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getTeacherCount() {
		return teacherCount;
	}
	public void setTeacherCount(Integer teacherCount) {
		this.teacherCount = teacherCount;
	}
	public Integer getParentCount() {
		return parentCount;
	}
	public void setParentCount(Integer parentCount) {
		this.parentCount = parentCount;
	}
	public Integer getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}
	public Integer getIsJoin() {
		return isJoin;
	}
	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
}
