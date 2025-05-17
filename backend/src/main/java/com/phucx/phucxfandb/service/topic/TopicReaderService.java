package com.phucx.phucxfandb.service.topic;

import com.phucx.phucxfandb.constant.NotificationTopic;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.entity.Topic;
import org.springframework.data.domain.Page;

public interface TopicReaderService {
    Page<TopicDTO> getTopics(int pageNumber, int pageSize);
    TopicDTO getTopic(long topicId);
    Topic getTopicEntity(NotificationTopic topic);
    TopicDTO getTopic(String topicName);
}