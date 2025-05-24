package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestParamsDTO extends PaginationParamsDTO {
    private String field = "createdAt";
    private Sort.Direction direction = Sort.Direction.DESC;
    private Boolean isRead;
}
