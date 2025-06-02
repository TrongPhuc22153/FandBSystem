package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.enums.MenuItemStatus;

public interface MenuItemService {
    ReservationDTO updateItemQuantity(String reservationId, String itemId, RequestMenuItemDTO request);

    ReservationDTO addItem(String reservationId, RequestMenuItemDTO request);

    ReservationDTO updateItemStatus(String reservationId, String itemId, RequestMenuItemDTO request);

    void updateItemStatus(String reservationId, MenuItemStatus originalStatus, MenuItemStatus statusToUpdate);

    void updateItemStatus(String reservationId, MenuItemStatus status);

    void cancelItem(String reservationId, String itemId);
}
