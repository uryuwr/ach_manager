package com.ch.module.achievement.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 成果等级
 * @author Chen on 2019-03-30-15:07
 */
@Data
@Entity
@Table(name = "ach_level")
public class AchLevel extends BaseDomain<Integer> {
    private Integer achType;
    private String levelName;
    private float point;
}
