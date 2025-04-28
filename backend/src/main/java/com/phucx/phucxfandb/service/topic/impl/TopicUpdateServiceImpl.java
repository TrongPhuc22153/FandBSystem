package com.phucx.phucxfandb.service.topic.impl;

import com.phucx.phucxfandb.dto.request.RequestTopicDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.entity.Topic;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.TopicMapper;
import com.phucx.phucxfandb.repository.TopicRepository;
import com.phucx.phucxfandb.service.topic.TopicUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicUpdateServiceImpl implements TopicUpdateService {
    private final TopicRepository topicRepository;
    private final TopicMapper mapper;

    @Override
    @Modifying
    @Transactional
    public TopicDTO updateTopic(Long topicId, RequestTopicDTO requestTopicDTO) {
        log.info("updateTopic(topicId={}, requestTopicDTO={})", topicId, requestTopicDTO);
        Topic existingTopic = topicRepository.findByTopicIdAndIsDeletedFalse(topicId)
                .orElseThrow(() -> new NotFoundException("Topic", topicId));
        mapper.updateTopic(requestTopicDTO, existingTopic);
        Topic updatedTopic = topicRepository.save(existingTopic);
        return mapper.toTopicDTO(updatedTopic);
    }

    @Override
    @Modifying
    @Transactional
    public TopicDTO createTopic(RequestTopicDTO createTopicDTO) {
        log.info("createTopic(createTopicDTO={})", createTopicDTO);
        if (topicRepository.existsByTopicName(createTopicDTO.getTopicName())) {
            throw new EntityExistsException("Topic " + createTopicDTO.getTopicName() + " already exists");
        }
        Topic topic = mapper.toTopic(createTopicDTO);
        Topic savedTopic = topicRepository.save(topic);
        return mapper.toTopicDTO(savedTopic);
    }

    @Override
    @Modifying
    @Transactional
    public List<TopicDTO> createTopics(List<RequestTopicDTO> createTopicDTOs) {
        log.info("createTopics(createTopicDTOs={})", createTopicDTOs);
        List<Topic> topicsToSave = createTopicDTOs.stream()
                .map(mapper::toTopic)
                .collect(Collectors.toList());
        return topicRepository.saveAll(topicsToSave).stream()
                .map(mapper::toTopicDTO)
                .collect(Collectors.toList());
    }
}