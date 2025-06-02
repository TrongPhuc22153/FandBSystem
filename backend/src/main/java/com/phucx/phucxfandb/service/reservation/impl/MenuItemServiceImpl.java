package com.phucx.phucxfandb.service.reservation.impl;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.MenuItem;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.enums.MenuItemStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.reservation.MenuItemService;
import com.phucx.phucxfandb.utils.PriceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final ProductReaderService productReaderService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public ReservationDTO updateItemQuantity(String reservationId, String itemId, RequestMenuItemDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        if (reservation.getStatus() == ReservationStatus.COMPLETED || reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify reservation with status: " + reservation.getStatus());
        }

        MenuItem existingItem = reservation.getMenuItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MenuItem.class.getSimpleName(), "id", itemId));

        if (!existingItem.getProduct().getProductId().equals(request.getProductId())) {
            throw new IllegalArgumentException("Provided product Id does not match the existing menu item");
        }

        if (request.getQuantity() == 0) {
            reservation.getMenuItems().remove(existingItem);
        } else {
            int quantityDelta = request.getQuantity() - existingItem.getQuantity();
            if (quantityDelta > 0) {
                Product product = productReaderService.getProductEntity(request.getProductId());
                if (product.getUnitsInStock() < quantityDelta) {
                    throw new IllegalArgumentException("Insufficient stock/availability for product Id: " + request.getProductId());
                }
            }

            existingItem.setQuantity(request.getQuantity());
            existingItem.setSpecialInstruction(request.getSpecialInstruction());
        }

        return saveAndMapReservation(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO addItem(String reservationId, RequestMenuItemDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        Product product = productReaderService.getProductEntity(request.getProductId());
        if (product.getUnitsInStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock/availability for product Id: " + request.getProductId());
        }

        MenuItem newItem = MenuItem.builder()
                .reservation(reservation)
                .product(product)
                .quantity(request.getQuantity())
                .price(product.getUnitPrice())
                .specialInstruction(request.getSpecialInstruction())
                .build();

        reservation.getMenuItems().add(newItem);

        return saveAndMapReservation(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO updateItemStatus(String reservationId, String itemId, RequestMenuItemDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        if (!(ReservationStatus.PARTIALLY_SERVED.equals(reservation.getStatus()) ||
                ReservationStatus.READY_TO_SERVE.equals(reservation.getStatus()))) {
            throw new IllegalStateException("Cannot update reservation item status for current reservation status: " + reservation.getStatus());
        }

        MenuItem item = reservation.getMenuItems().stream()
                .filter(ri -> ri.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MenuItem.class.getSimpleName(), "id", itemId));

        item.setStatus(request.getStatus());
        reservation.setStatus(ReservationStatus.PARTIALLY_SERVED);

        return saveAndMapReservation(reservation);
    }

    @Override
    @Transactional
    public void updateItemStatus(String reservationId, MenuItemStatus originalStatus, MenuItemStatus statusToUpdate) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        reservation.getMenuItems().forEach(item -> {
            if (item.getStatus().equals(originalStatus)) {
                item.setStatus(statusToUpdate);
            }
        });
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void updateItemStatus(String reservationId, MenuItemStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        reservation.getMenuItems().forEach(item -> {
            item.setStatus(status);
        });
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void cancelItem(String reservationId, String itemId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        MenuItem itemToCancel = reservation.getMenuItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MenuItem.class.getSimpleName(), "id", itemId));

        if (!EnumSet.of(MenuItemStatus.PENDING, MenuItemStatus.PREPARING).contains(itemToCancel.getStatus())) {
            throw new IllegalStateException("Only items in PENDING or PREPARING status can be canceled.");
        }

        itemToCancel.setStatus(MenuItemStatus.CANCELED);

        BigDecimal newTotal = PriceUtils.calculateReservationTotalPrice(reservation.getMenuItems());
        reservation.setTotalPrice(newTotal);
        reservation.getPayment().setAmount(newTotal);
        reservationRepository.save(reservation);
    }

    private ReservationDTO saveAndMapReservation(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.setStatus(ReservationStatus.PREPARING);
        }

        reservation.setTotalPrice(PriceUtils.calculateReservationTotalPrice(reservation.getMenuItems()));
        reservation.getPayment().setAmount(reservation.getTotalPrice());
        
        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updated);
    }
}

