package com.jiuchunjiaoyu.micro.wzb.background.dto.page;

import java.util.List;

/**
 * 用来接收springdata的分页数据
 */
public class PageDTO<T>{

	private List<T> content;
	
	private Long totalPages;
	
	private Long totalElements;
	
	private Boolean last;
	
	private Integer numberOfElements;
	
	private Boolean first;
	
	private Integer size;
	
	private Integer number;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getPageNo(){
		return getNumber()+1;
	}
}
