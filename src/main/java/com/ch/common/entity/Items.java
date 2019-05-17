package com.ch.common.entity;


import lombok.Data;

import java.util.List;

@Data
public class Items<T> {
	private Long count;
	private List<T> list;
	public static <T> Items<T> of(List<T> list) {
		Items<T> items = new Items<>();
		items.setList(list);
		return items;
	}

	public static <T> Items<T> of(List<T> list,long count) {
		Items<T> items = new Items<>();
		items.setList(list);
		items.setCount(count);
		return items;
	}


}
