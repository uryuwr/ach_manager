package com.ch.common.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseDomain<I extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 36)
    private I id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "datetime")
    private Date createTime;

    private boolean deleted;

}

