package com.ch.module.achievement.domain;

import com.ch.common.entity.BaseDomain;
import com.ch.utils.ExcelAnno;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 成果类
 * @author Chen on 2019-03-30-15:07
 */
@Data
@Entity
@Table(name = "achievement")
public class Achievement extends BaseDomain<Integer> {
    @Column(columnDefinition = "tinyint")
    private Integer domainType;

    @Column(columnDefinition = "varchar(20)")
    private String title;

    @Column(columnDefinition = "varchar(20)")
    private String author;

    @Column(columnDefinition = "tinyint")
    private Integer auditState;

    @Column(columnDefinition = "tinyint")
    private Integer levelId;

    @Column(columnDefinition = "varchar(8)")
    private String createUser;

    @Column(columnDefinition = "text")
    private String attachment;

    @Column(columnDefinition = "tinyint")
    private Integer achType;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "float")
    private Float proportion;

    @Column(columnDefinition = "varchar(8)")
    private String auditUser;
}
