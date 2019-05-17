package com.ch.module.achievement.repository;

import com.ch.common.repository.BaseRepository;
import com.ch.module.achievement.domain.AchDomain;
import org.springframework.stereotype.Repository;

/**
 * @author Chen on 2019-05-11-11:47
 */
@Repository
public interface AchDomainRepository extends BaseRepository<AchDomain,Integer> {
}
