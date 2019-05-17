package com.ch.module.user.dto;

import com.ch.common.config.MyException;
import com.ch.module.user.domain.Registry;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;


/**
 * @author ch265357 2019-05-07 17:26
 */
@Data
public class UserInfo extends Registry{
	private String nodeName;

	public static UserInfo setUserInfo(Registry registry) {
		UserInfo userInfo = new UserInfo();
		/*userInfo.setUserName(registry.getUserName());
		userInfo.setRealName(registry.getRealName());
		userInfo.setPassword(registry.getPassword());
		userInfo.setRole(registry.getRole());
		userInfo.setEmail(registry.getEmail());
		userInfo.setAddress(registry.getAddress());*/
		try {
			BeanUtils.copyProperties(userInfo,registry);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new MyException("400","BeanUtils error");
		}
		return userInfo;
	}
}
