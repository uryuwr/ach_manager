package com.ch.module.achievement.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 业绩信息表
 * @author Chen on 2019-03-30-15:17
 */
@Data
@Entity
@Table(name = "ach_info")
public class AchInfo extends BaseDomain<Integer> {
    private Integer achId;
    private Integer achType;
    private String title;
    private Float point;
    private String authors;
    private String createUser;
}
