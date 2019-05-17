package com.ch.module.audit.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Chen on 2019-02-16-13:57
 */
@Data
@Entity
@Table(name = "audit")
public class Audit extends BaseDomain<Integer> {
    private Integer achId;
    private Integer achType;
    private String title;
    private String createUser;
    private Integer auditState;
    private String auditUser;
    private Date finishTime;
    private String auditOpinion;
    private float point;
}
