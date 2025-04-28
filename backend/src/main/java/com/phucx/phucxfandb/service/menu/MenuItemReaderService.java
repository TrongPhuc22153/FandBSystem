package com.phucx.phucxfandb.service.menu;

import com.phucx.phucxfandb.dto.response.MenuItemDTO;

import java.util.List;

public interface MenuItemReaderService {

    MenuItemDTO getMenuItemById(String menuItemId);
    List<MenuItemDTO> getAllMenuItems();
}
