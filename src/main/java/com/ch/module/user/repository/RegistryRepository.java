package com.ch.module.user.repository;

import com.ch.common.repository.BaseRepository;
import com.ch.module.user.domain.Registry;
import org.springframework.stereotype.Repository;

/**
 * @author Chen on 2019-02-06-23:52
 */
@Repository
public interface RegistryRepository extends BaseRepository<Registry,Integer> {
    Registry findByUserNameAndDeleted(String userName,boolean delete);
}
