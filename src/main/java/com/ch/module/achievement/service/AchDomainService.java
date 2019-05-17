package com.ch.module.achievement.service;

import com.ch.common.service.BaseService;
import com.ch.module.achievement.domain.AchDomain;
import org.springframework.stereotype.Service;

/**
 * @author Chen on 2019-05-11-11:48
 */
@Service
public class AchDomainService extends BaseService<AchDomain,Integer> {
    public String getDomainName(Integer id) {
        return this.findStrictOne(id).getDomainName();
    }
}
