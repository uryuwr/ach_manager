package com.ch.common.entity;

import lombok.Data;

@Data
public class PageCondition {
	private Integer offset;
	private Integer limit;
	private Boolean count;
	private Select select;
	public PageCondition(Integer offset, Integer limit, Boolean count, Select select) {
		this.offset = offset;
		this.limit = limit;
		this.count = count;
		this.select = select;
	}
}
