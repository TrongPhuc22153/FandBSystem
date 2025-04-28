package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T>{
    String error;
    String message;
    Map<String, List<String>> fields;
    T data;
}
