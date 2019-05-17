package com.ch.module.achievement.dto;

import com.ch.module.achievement.domain.Achievement;
import com.ch.utils.ExcelAnno;
import lombok.Data;

import java.util.Date;


/**
 * @author Chen on 2019-05-11-9:38
 */
@Data
public class AchievementDto extends Achievement {
    private String domainTypeName;

    private String achTypeName;

    private String levelName;

    private String auditStateName;

    private String createUserName;

    private float point;

}
