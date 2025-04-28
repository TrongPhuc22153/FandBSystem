package com.phucx.phucxfandb.service.menu;

import com.phucx.phucxfandb.dto.request.RequestMenuDTO;
import com.phucx.phucxfandb.dto.response.MenuDTO;

public interface MenuUpdateService {
    MenuDTO createMenu(RequestMenuDTO createMenuRequest);
    MenuDTO updateMenu(String menuId, RequestMenuDTO updateMenuRequest);
    void deleteMenu(String menuId);

}
