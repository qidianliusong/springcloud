package com.jiuchunjiaoyu.micro.aggregate.wzb.dto;

import java.io.Serializable;

public class TestUserDto implements Serializable{

	private static final long serialVersionUID = -8123885655043752317L;
	
	private long userId;
	private String name;
	private int age;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "TestUserDto [userId=" + userId + ", name=" + name + ", age=" + age + "]";
	}
	
}
