package com.phucx.phucxfandb.service.topic.impl;

import com.phucx.phucxfandb.constant.NotificationTopic;
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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicReaderServiceImpl implements TopicReaderService {
    private final TopicRepository topicRepository;
    private final TopicMapper mapper;

    @Override
    public Page<TopicDTO> getTopics(int pageNumber, int pageSize) {
        log.info("getTopics(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Topic> topics = topicRepository.findByIsDeletedFalse(page);
        return topics.map(mapper::toTopicDTO);
    }

    @Override
    public TopicDTO getTopic(long topicId) {
        log.info("getTopic(topicId={})", topicId);
        Topic topic = topicRepository.findByTopicIdAndIsDeletedFalse(topicId)
                .orElseThrow(() -> new NotFoundException("Topic", topicId));
        return mapper.toTopicDTO(topic);
    }

    @Override
    public Topic getTopicEntity(NotificationTopic topic) {
        log.info("getTopic(topic={})", topic);
        return topicRepository.findByTopicNameAndIsDeletedFalse(topic.name())
                .orElseThrow(() -> new NotFoundException("Topic", "name", topic.name()));
    }

    @Override
    public TopicDTO getTopic(String topicName) {
        log.info("getTopic(topicName={})", topicName);
        Topic topic = topicRepository.findByTopicNameAndIsDeletedFalse(topicName)
                .orElseThrow(() -> new NotFoundException("Topic", topicName));
        return mapper.toTopicDTO(topic);
    }
}