package com.phucx.phucxfandb.service.menu;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.response.MenuItemDTO;

public interface MenuItemUpdateService {
    MenuItemDTO createMenuItem(RequestMenuItemDTO createMenuItemRequest);
    MenuItemDTO updateMenuItem(String menuItemId, RequestMenuItemDTO updateMenuItemRequest);
    void deleteMenuItem(String menuItemId);
}
