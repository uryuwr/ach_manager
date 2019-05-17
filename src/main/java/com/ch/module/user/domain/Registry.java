package com.ch.module.user.domain;

import com.ch.common.entity.BaseDomain;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * @author Chen on 2019-02-04-21:01
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "registry")
public class Registry extends BaseDomain<Integer> {
    @Column(columnDefinition = "varchar(10)",unique = true)
    private String userName;

    @Column(columnDefinition = "varchar(8) default '游客' ")
    private String realName;

    @Column(columnDefinition = "varchar(16)")
    private String password;

    @Column(columnDefinition = "varchar(10) default 'USER' ")
    private String role;

    @Column(columnDefinition = "varchar(30) default '' ")
    private String email;

    @Column(columnDefinition = "varchar(30) default '' ")
    private String address;

    @Column(columnDefinition = "varchar(11) default '' ")
    private String phone;

    @Column(columnDefinition = "tinyint default 1 ")
    private Integer nodeId;

}
