package com.phucx.phucxfandb.service.topic.impl;

import com.phucx.phucxfandb.constant.NotificationTopic;
import com.phucx.phucxfandb.dto.request.TopicRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.entity.Topic;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.TopicMapper;
import com.phucx.phucxfandb.repository.TopicRepository;
import com.phucx.phucxfandb.service.topic.TopicReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicReaderServiceImpl implements TopicReaderService {
    private final TopicRepository topicRepository;
    private final TopicMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> getTopics(TopicRequestParamsDTO params) {
        Pageable page = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        Page<Topic> topics = topicRepository.findByIsDeletedFalse(page);
        return topics.map(mapper::toTopicDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TopicDTO getTopic(long topicId) {
        Topic topic = topicRepository.findByTopicIdAndIsDeletedFalse(topicId)
                .orElseThrow(() -> new NotFoundException(Topic.class.getSimpleName(), topicId));
        return mapper.toTopicDTO(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getTopicEntity(NotificationTopic topic) {
        return topicRepository.findByTopicNameAndIsDeletedFalse(topic.name())
                .orElseThrow(() -> new NotFoundException(Topic.class.getSimpleName(), "name", topic.name()));
    }
}