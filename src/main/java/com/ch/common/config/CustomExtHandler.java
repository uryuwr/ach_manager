package com.ch.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExtHandler {
	private static final Logger LOG = LoggerFactory.getLogger(CustomExtHandler.class);

	//捕获全局异常，处理所有不可知的异常
	@ExceptionHandler(value=Exception.class)
	//@ResponseBody
	public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		LOG.error("msg:{},url:{}", e.getMessage(), request.getRequestURL());

		Map<String, Object> map = new HashMap<>();
		map.put("code", 100);

		map.put("msg", e.getMessage());
		map.put("url", request.getRequestURL());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return map;
	}

	//自定义异常
	//需要添加thymeleaf依赖
	//路径：src/main/resources/templates/error.html
	@ExceptionHandler(value=MyException.class)
	public Object handleMyException(MyException e, HttpServletRequest request, HttpServletResponse response) {
		//返回Json数据，由前端进行界面跳转
		Map<String, Object> map = new HashMap<>();
		map.put("code", e.getCode());
		map.put("msg", e.getMsg());
		map.put("url", request.getRequestURL());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return map;

		/*//进行页面跳转
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error.html");
		modelAndView.addObject("msg", e.getMsg());
		return modelAndView;*/
	}
}
