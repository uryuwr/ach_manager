package com.ch.module.achievement.repository;

import com.ch.common.repository.BaseRepository;
import com.ch.module.achievement.domain.Achievement;
import org.springframework.stereotype.Repository;

/**
 * @author Chen on 2019-05-11-9:40
 */
@Repository
public interface AchievementRepository extends BaseRepository<Achievement,Integer> {
}
