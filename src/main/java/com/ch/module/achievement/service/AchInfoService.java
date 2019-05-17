package com.ch.module.achievement.service;

import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.service.BaseService;
import com.ch.module.achievement.domain.AchInfo;
import com.ch.module.achievement.repository.AchInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Chen on 2019-05-11-11:56
 */
@Service
public class AchInfoService extends BaseService<AchInfo,Integer> {
    @Autowired
    private AchInfoRepository achInfoRepository;

    public boolean hadAch(Integer achId) {
        AchInfo achInfo = achInfoRepository.findByAchId(achId);
        return achInfo != null;
    }

    public Items<AchInfo> getAchInfos(PageCondition page, AchInfo achievement) {
        Items<AchInfo> items = null;
        try {
            items = this.list(page,achievement);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return items;
    }
}
