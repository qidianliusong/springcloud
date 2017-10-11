package com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result;
/**
 * 接收返回的list
 */

import java.util.List;

public class ResultList<T> {

	private List<T> list;
	
	private Integer count;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
