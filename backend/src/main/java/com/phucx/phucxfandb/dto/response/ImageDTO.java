package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    String imageUrl;

}
