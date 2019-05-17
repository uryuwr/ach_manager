package com.ch.module.user.service;

import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.service.BaseService;
import com.ch.module.user.domain.Registry;
import com.ch.module.user.repository.RegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ch265357 2019-04-03 14:21
 */
@Service
public class RegistryService extends BaseService<Registry,Integer> {
	@Autowired
	private RegistryRepository registryRepository;

	public Items<Registry> getRegistry(PageCondition page,Registry registry) {
		Items<Registry> items = null;
		try {
			items = this.list(page,registry);
		} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return items;
	}

	public Registry getSessionUserInfo(HttpServletRequest request) {
		return (Registry) request.getSession().getAttribute("user_info");
	}

	public Registry findByUserName(String userName) {
		return registryRepository.findByUserNameAndDeleted(userName,false);
	}
}
