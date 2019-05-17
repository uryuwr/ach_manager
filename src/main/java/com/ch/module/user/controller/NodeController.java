package com.ch.module.user.controller;

import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.entity.Select;
import com.ch.module.user.domain.Node;
import com.ch.module.user.repository.NodeRepository;
import com.ch.module.user.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ch265357 2019-05-07 14:51
 */
@RestController
@RequestMapping("/v0.1/node")
public class NodeController {
	@Autowired
	private NodeService nodeService;

	@Autowired
	private NodeRepository nodeRepository;

	@GetMapping("/{id}")
	public Node getNode(@PathVariable int id) {
		Node node = nodeService.findOne(id);
		Node ret = new Node();
		ret.setId(node.getId());
		ret.setNodeName(node.getNodeName());
		return ret;
	}

	@GetMapping
	public List<Node> findALL() {
		return nodeService.findAll();
	}

	@GetMapping("/page")
	public Items<Node> getPage(@RequestParam(required = false) Integer offset,
							   @RequestParam(required = false) Integer limit,
							   @RequestParam(required = false) Boolean count,
							   @RequestParam(required = false) String selects) {
		Select select = null;
		if (selects != null) {
			select = Select.getSelect(selects,false);
		}
		PageCondition pageCondition = new PageCondition(offset, limit, count, select);
		Node node = new Node();
		return nodeService.getNodes(pageCondition, node);
	}

	@PutMapping
	public void saveNode(@RequestBody Node node) {
		Integer id = node.getId();
		if (null == id) {
			nodeService.add(node);
		} else {
			Node po = nodeService.findOne(id);
			if (po != null) {
				po.setNodeName(node.getNodeName());
				nodeRepository.save(po);
			}
		}
	}

	@DeleteMapping("/{id}")
	public void deleteNode(@PathVariable Integer id) {
		nodeService.delete(id);
	}
}
