package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 学生信息
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StudentDTO {

	private Long id;
	private String name;
	private String avatarUrl;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date birth;
	private Integer gender;
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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
}
