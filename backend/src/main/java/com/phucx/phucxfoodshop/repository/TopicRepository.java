package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.entity.Topic;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>{
    
}
