package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopicRequestParamsDTO extends PaginationParamsDTO {
    private String field = "topicName";
}
