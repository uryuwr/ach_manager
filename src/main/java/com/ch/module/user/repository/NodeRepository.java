package com.ch.module.user.repository;

import com.ch.common.repository.BaseRepository;
import com.ch.module.user.domain.Node;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends BaseRepository<Node,Integer> {
}
