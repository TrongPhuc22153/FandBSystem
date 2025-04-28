package com.phucx.phucxfandb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMenuDTO {

    private String menuId;

    private String name;

    private String description;

    private List<RequestMenuItemDTO> menuItems;

}
