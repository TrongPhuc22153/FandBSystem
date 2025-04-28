package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestTopicDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDTO toTopicDTO(Topic topic);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "topicId", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Topic toTopic(RequestTopicDTO requestTopicDTO);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "topicId", ignore = true)
    void updateTopic(RequestTopicDTO requestTopicDTO, @MappingTarget Topic topic);
}
