package com.ch.module.achievement.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 领域类型
 * @author Chen on 2019-03-30-15:04
 */
@Data
@Entity
@Table(name = "ach_domain")
public class AchDomain extends BaseDomain<Integer> {
    private String domainName;
}
