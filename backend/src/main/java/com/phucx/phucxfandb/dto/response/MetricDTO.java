package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MetricDTO {
    List<String> labels;
    List<Long> values;
}
