package com.phucx.phucxfandb.service.topic;

import com.phucx.phucxfandb.dto.request.RequestTopicDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import java.util.List;

public interface TopicUpdateService {
    TopicDTO updateTopic(Long topicId, RequestTopicDTO requestTopicDTO);
    TopicDTO createTopic(RequestTopicDTO createTopicDTO);
    List<TopicDTO> createTopics(List<RequestTopicDTO> createTopicDTOs);
}