package com.ch.common.repository;

import com.ch.common.entity.BaseDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T extends BaseDomain<I>, I extends Serializable> extends JpaRepository<T, I> {
    
    @Override
    List<T> findAll();
  
}