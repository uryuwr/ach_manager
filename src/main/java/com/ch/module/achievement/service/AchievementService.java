package com.ch.module.achievement.service;

import com.ch.common.entity.Items;
import com.ch.common.entity.PageCondition;
import com.ch.common.service.BaseService;
import com.ch.module.achievement.domain.AchInfo;
import com.ch.module.achievement.domain.AchLevel;
import com.ch.module.achievement.domain.Achievement;
import com.ch.module.achievement.dto.AchievementDto;
import com.ch.module.user.domain.Node;
import com.ch.module.user.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen on 2019-05-11-9:40
 */
@Service
public class AchievementService extends BaseService<Achievement,Integer> {
    @Autowired
    private AchInfoService achInfoService;
    @Autowired
    private RegistryService registryService;
    @Autowired
    private AchDomainService achDomainService;
    @Autowired
    private AchLevelService achLevelService;

    public Items<Achievement> getAchievements(PageCondition page, Achievement achievement) {
        Items<Achievement> items = null;
        try {
            items = this.list(page,achievement);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return items;
    }

    public String getAchType(Integer achType) {
        switch (achType) {
            case 0:
                return "论文";
            case 1:
                return "论著";
            case 2:
                return "专利";
            case 3:
                return "项目";
        }
        return "论文";
    }

    public List<AchievementDto> getExcelDatas() {
        List<AchInfo> achInfoList = achInfoService.findAll();
        List<AchievementDto> ret = new ArrayList<>();
        for (AchInfo achInfo : achInfoList) {
            Integer achId =  achInfo.getAchId();
            Achievement achievement = this.findStrictOne(achId);
            AchievementDto achievementDto = new AchievementDto();
            achievementDto.setId(achId);
            achievementDto.setAchTypeName(this.getAchType(achInfo.getAchType()));
            achievementDto.setTitle(achInfo.getTitle());
            achievementDto.setPoint(achInfo.getPoint());
            achievementDto.setAuthor(achInfo.getAuthors());
            achievementDto.setCreateTime(achInfo.getCreateTime());
            achievementDto.setCreateUserName(registryService.findByUserName(achInfo.getCreateUser()).getRealName());
            achievementDto.setDomainTypeName(achDomainService.getDomainName(achievement.getDomainType()));
            achievementDto.setLevelName(achLevelService.getlevelName(achievement.getLevelId()));
            ret.add(achievementDto);
        }
        return ret;
    }
}
