package com.ch.module.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.ch.common.Listeners.TestEvent;
import com.ch.common.config.MyException;
import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.entity.Select;
import com.ch.module.user.domain.Registry;
import com.ch.module.user.dto.UserInfo;
import com.ch.module.user.repository.RegistryRepository;
import com.ch.module.user.service.NodeService;
import com.ch.module.user.service.RegistryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen on 2019
 */
@RestController
@RequestMapping("/v0.1/user")
public class UserController {
	@Autowired
	private RegistryRepository registryRepository;

	@Autowired
	private RegistryService registryService;

	@Autowired
	private NodeService nodeService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping(value = "/{id}")
	public String getUserInfoById(@PathVariable Integer id) {
		Registry po = registryService.findOne(id);
		if (null == po) {
			throw new MyException("400","用户不存在!");
		}
		return JSONObject.toJSONString(po);
	}

	@GetMapping(value = "/user_info")
	public Registry test(HttpServletRequest request) {
		return registryService.getSessionUserInfo(request);
	}

	@PostMapping("/registry")
	public void registry(@RequestBody Registry registry){
		Registry po = registryRepository.findByUserNameAndDeleted(registry.getUserName(),false);
		String userName = registry.getUserName();
		String password = registry.getPassword();
		if (StringUtils.isEmpty(userName)) {
			throw new MyException("400","账号不能为空！");
		} else if (userName.length() < 3 || userName.length() > 20){
			throw new MyException("400","账号长度必须大于3，小于20");
		}

		if (StringUtils.isEmpty(password)) {
			throw new MyException("400","密码不能为空！");
		} else if (password.length() < 6 || password.length() > 16){
			throw new MyException("400","密码长度必须大于6，小于16");
		}

		if (po != null) {
			throw new MyException("400","该用户名已存在！");
		}
		registryService.add(registry);
	}

	@PostMapping("/login")
	public boolean login(@RequestBody Registry registry, HttpServletRequest request) {
		Registry po = registryRepository.findByUserNameAndDeleted(registry.getUserName(),false);
		if (null == po) {
			throw new MyException("400","用户不存在!");
		}
		if (po.getPassword().equals(registry.getPassword())) {
			request.getSession().removeAttribute("user_info");
			request.getSession().setAttribute("user_info", po);
			return true;
		}
		return false;
	}

	@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("user_info");
		try {
			response.sendRedirect("/view/login.html");
		} catch (IOException e) {
			logger.error("io error", e);
		}
	}

	@Autowired
	private ApplicationContext publisher;

	@PostMapping("/audit")
	public void hello() {
		TestEvent event = new TestEvent(this, "123456");
		publisher.publishEvent(event);
		System.out.println("发布成功");
	}

	@PutMapping("/{user_name}")
	public Registry updateUserInfo(@PathVariable("user_name") String userName, @RequestBody Registry registry) {
		//将数据库数据拉到JVM内存中
		Registry po = registryRepository.findByUserNameAndDeleted(userName,false);
		if (null == po) {
			throw new MyException("400","该用户不存在，或已删除！");
		}
		//获取前端传入的值
		String password = registry.getPassword();
		String realName = registry.getRealName();
		String address = registry.getAddress();
		String mail = registry.getEmail();
		String phone = registry.getPhone();
		String role = registry.getRole();
		Integer nodeId = registry.getNodeId();
		//执行更新数据的操作
		if (StringUtils.isNotEmpty(password)) {
			po.setPassword(password);
		}
		if (StringUtils.isNotEmpty(realName)) {
			po.setRealName(realName);
		}
		if (StringUtils.isNotEmpty(address)) {
			po.setAddress(address);
		}
		if (StringUtils.isNotEmpty(mail)) {
			po.setEmail(mail);
		}
		if (StringUtils.isNotEmpty(phone)) {
			po.setPhone(phone);
		}
		if (StringUtils.isNotEmpty(role)) {
			po.setRole(role);
		}
		if (null != nodeId) {
			po.setNodeId(nodeId);
		}
		//持久化数据
		return registryRepository.save(po);
	}


	@GetMapping("/page")
	public Items<UserInfo> getPage(@RequestParam(required = false) Integer offset,
								   @RequestParam(required = false) Integer limit,
								   @RequestParam(required = false) Boolean count,
								   @RequestParam(required = false) String selects) {
		Select select = null;
		if (selects != null) {
			select = Select.getSelect(selects,false);
		}
		PageCondition pageCondition = new PageCondition(offset, limit, count, select);
		Registry registry = new Registry();
		Items<Registry> registryItems = registryService.getRegistry(pageCondition,registry);
		List<Registry> registryList = registryItems.getList();
		List<UserInfo> ret = new ArrayList<>();
		for (Registry po : registryList) {
			UserInfo userInfo = UserInfo.setUserInfo(po);
			String nodeName = nodeService.findOne(po.getNodeId()).getNodeName();
			userInfo.setNodeName(nodeName);
			ret.add(userInfo);
		}
		return Items.of(ret,registryItems.getCount());

	}
}
