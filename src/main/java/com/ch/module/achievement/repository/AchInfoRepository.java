package com.ch.module.achievement.repository;

import com.ch.common.repository.BaseRepository;
import com.ch.module.achievement.domain.AchInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Chen on 2019-05-11-11:56
 */
@Repository
public interface AchInfoRepository extends BaseRepository<AchInfo,Integer> {
    AchInfo findByAchId(Integer achId);
}
