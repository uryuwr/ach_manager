package com.ch.common.config;

import lombok.Data;

@Data
public class MyException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	public MyException(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}
}
