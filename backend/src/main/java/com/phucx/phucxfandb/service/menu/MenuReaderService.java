package com.phucx.phucxfandb.service.menu;

import com.phucx.phucxfandb.dto.response.MenuDTO;

import java.util.List;

public interface MenuReaderService {
    MenuDTO getMenuById(String menuId);
    List<MenuDTO> getAllMenus(int pageNumber, int pageSize);
}
