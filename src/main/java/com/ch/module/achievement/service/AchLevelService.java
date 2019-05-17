package com.ch.module.achievement.service;

import com.ch.common.service.BaseService;
import com.ch.module.achievement.domain.AchLevel;
import org.springframework.stereotype.Service;

/**
 * @author Chen on 2019-05-11-11:51
 */
@Service
public class AchLevelService extends BaseService<AchLevel,Integer> {
    public String getlevelName(Integer levelId) {
        return this.findStrictOne(levelId).getLevelName();
    }

    public float getPoint(Integer levelId,float proportion) {
        AchLevel achLevel = this.findStrictOne(levelId);
        return achLevel.getPoint() * proportion;
    }
}
