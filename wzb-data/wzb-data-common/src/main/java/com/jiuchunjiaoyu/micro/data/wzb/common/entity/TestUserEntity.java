package com.jiuchunjiaoyu.micro.data.wzb.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="test_user")
public class TestUserEntity {

	@Id
    @GeneratedValue
    @ApiModelProperty(value="用户ID",name="userId",required=false,dataType="long")
    private Long userId;

    @Column
    @ApiModelProperty(value="用户名",name="name",required=false,dataType="String")
    private String name;

    @Column
    @ApiModelProperty(value="用户年龄",name="age",required=false,dataType="int")
    private Integer age;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
    
}
