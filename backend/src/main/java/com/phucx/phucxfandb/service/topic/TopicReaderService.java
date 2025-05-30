package com.phucx.phucxfandb.service.topic;

import com.phucx.phucxfandb.enums.NotificationTopic;
import com.phucx.phucxfandb.dto.request.TopicRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.entity.Topic;
import org.springframework.data.domain.Page;

public interface TopicReaderService {
    Page<TopicDTO> getTopics(TopicRequestParamsDTO params);
    TopicDTO getTopic(long topicId);
    Topic getTopicEntity(NotificationTopic topic);
}