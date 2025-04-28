package com.phucx.phucxfandb.dto.response;

import com.phucx.phucxfandb.entity.MenuItem;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MenuDTO {

   String menuId;
   String name;
   String description;
   List<MenuItem> menuItems;
}
