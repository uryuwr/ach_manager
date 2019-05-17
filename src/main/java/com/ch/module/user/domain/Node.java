package com.ch.module.user.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ch265357 2019-05-07 14:42
 */
@Data
@Entity
@Table(name = "node")
public class Node extends BaseDomain<Integer> {
	@Column(columnDefinition = "varchar(10)",unique = true)
	private String nodeName;
}
