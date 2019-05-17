package com.ch.module.user.service;

import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.service.BaseService;
import com.ch.module.user.domain.Node;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ch265357 2019-05-07 14:54
 */
@Service
public class NodeService extends BaseService<Node,Integer> {
	public Items<Node> getNodes(PageCondition page, Node node) {
		Items<Node> items = null;
		try {
			items = this.list(page,node);
		} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return items;
	}
}
