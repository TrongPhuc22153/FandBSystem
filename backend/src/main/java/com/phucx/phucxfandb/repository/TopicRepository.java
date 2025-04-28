package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{
    @Transactional(readOnly = true)
    Page<Topic> findByIsDeletedFalse(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<Topic> findByTopicIdAndIsDeletedFalse(long id);

    @Transactional(readOnly = true)
    Optional<Topic> findByTopicNameAndIsDeletedFalse(String topicName);

    @Transactional(readOnly = true)
    boolean existsByTopicName(String topicName);
}
