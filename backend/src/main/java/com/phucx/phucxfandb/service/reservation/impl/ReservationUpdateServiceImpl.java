package com.phucx.phucxfandb.service.reservation.impl;

import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.enums.MenuItemStatus;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.MenuItemMapper;
import com.phucx.phucxfandb.mapper.ReservationMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import com.phucx.phucxfandb.service.table.TableReaderService;
import com.phucx.phucxfandb.utils.PriceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationUpdateServiceImpl implements ReservationUpdateService {
    private final CustomerReaderService customerReaderService;
    private final EmployeeReaderService employeeReaderService;
    private final ReservationRepository reservationRepository;
    private final TableReaderService tableReaderService;
    private final ProductReaderService productReaderService;
    private final ProductUpdateService productUpdateService;
    private final ReservationMapper reservationMapper;
    private final MenuItemMapper menuItemMapper;

    @Override
    @Transactional
    public ReservationDTO createCustomerReservation(String username, RequestReservationDTO requestReservationDTO) {
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);
        TableEntity table = tableReaderService
                .getTableEntity(requestReservationDTO.getTableId());

        Reservation newReservation = reservationMapper.toCustomerReservation(
                requestReservationDTO,
                table,
                customer);

        List<MenuItem> newMenuItems = requestReservationDTO.getMenuItems().stream().map(requestMenuItem -> {
            Product product = productReaderService.getProductEntity(requestMenuItem.getProductId());

            if(requestMenuItem.getQuantity()>product.getUnitsInStock()){
                throw new IllegalArgumentException("Insufficient stock for product ID: " + requestMenuItem.getProductId());
            }
            Product updated = productUpdateService.updateProductInStock(requestMenuItem.getProductId(), requestMenuItem.getQuantity());
            return menuItemMapper.toMenuItem(requestMenuItem, newReservation, updated);
        }).collect(Collectors.toList());

        BigDecimal totalPrice = PriceUtils.calculateReservationTotalPrice(newMenuItems);

        newReservation.setTotalPrice(totalPrice);
        newReservation.setStatus(ReservationStatus.PENDING);
        newReservation.setMenuItems(newMenuItems);

        Payment payment = Payment.builder()
                .customer(customer)
                .amount(totalPrice)
                .reservation(newReservation)
                .build();

        newReservation.setPayment(payment);

        Reservation saved = reservationRepository.save(newReservation);
        return reservationMapper.toReservationDTO(saved);
    }

    @Override
    @Transactional
    public ReservationDTO createEmployeeReservation(String username, RequestReservationDTO requestReservationDTO) {
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);
        TableEntity table = tableReaderService
                .getTableEntity(requestReservationDTO.getTableId());

        Reservation newReservation = reservationMapper.toEmployeeReservation(
                requestReservationDTO,
                table,
                employee);

        newReservation.setStatus(ReservationStatus.PENDING);

        List<MenuItem> newMenuItems = requestReservationDTO.getMenuItems().stream().map(requestMenuItem -> {
            Product product = productReaderService.getProductEntity(requestMenuItem.getProductId());

            if(requestMenuItem.getQuantity()>product.getUnitsInStock()){
                throw new IllegalArgumentException("Insufficient stock for product ID: " + requestMenuItem.getProductId());
            }
            Product updatedProduct = productUpdateService.updateProductInStock(requestMenuItem.getProductId(), requestMenuItem.getQuantity());
            return menuItemMapper.toMenuItem(requestMenuItem, newReservation, updatedProduct);
        }).collect(Collectors.toList());
        BigDecimal totalPrice = PriceUtils.calculateReservationTotalPrice(newMenuItems);
        newReservation.setTotalPrice(totalPrice);
        newReservation.setMenuItems(newMenuItems);
        newReservation.setStatus(ReservationStatus.PENDING);

        Payment payment = Payment.builder()
                .employee(employee)
                .amount(totalPrice)
                .reservation(newReservation)
                .build();

        newReservation.setPayment(payment);

        Reservation saved = reservationRepository.save(newReservation);
        return reservationMapper.toReservationDTO(saved);
    }

    @Override
    public ReservationDTO updateReservation(String reservationId, ReservationStatus reservationStatus, PaymentStatus paymentStatus) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
        reservation.setStatus(reservationStatus);
        reservation.getPayment().setStatus(paymentStatus);
        Reservation updatedOrder = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updatedOrder);
    }

    @Override
    @Transactional
    public ReservationDTO updateReservation(String username, String reservationId, RequestReservationDTO requestReservationDTO) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));

        validateReservationStatus(reservation);
        processReservationItems(reservation, requestReservationDTO.getMenuItems());
        updateReservationStatus(reservation);
        updateReservationFinancials(reservation);

        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updated);
    }

    private void validateReservationStatus(Reservation reservation) {
        if (ReservationStatus.CANCELLED.equals(reservation.getStatus())) {
            throw new IllegalStateException("Cannot modify reservation with status: " + reservation.getStatus());
        }
    }

    private void processReservationItems(Reservation reservation, List<RequestMenuItemDTO> requestItems) {
        for (RequestMenuItemDTO requestItem : requestItems) {
            validateRequestItemDetail(requestItem);
            Optional<MenuItem> existingItem = findExistingReservationItem(reservation, requestItem);
            handleReservationItem(reservation, requestItem, existingItem);
        }
    }

    private void validateRequestItemDetail(RequestMenuItemDTO requestItem) {
        if (requestItem.getProductId() == null || requestItem.getQuantity() == null || requestItem.getQuantity() < 0) {
            throw new IllegalArgumentException("menuItemId and non-negative quantity are required for reservation items");
        }
    }

    private Optional<MenuItem> findExistingReservationItem(Reservation reservation, RequestMenuItemDTO requestItem) {
        if (requestItem.getId() != null) {
            return reservation.getMenuItems().stream()
                    .filter(ri -> ri.getId().equals(requestItem.getId()))
                    .findFirst();
        }
        return reservation.getMenuItems().stream()
                .filter(ri -> ri.getProduct().getProductId().equals(requestItem.getProductId()))
                .findFirst();
    }

    private void handleReservationItem(Reservation reservation, RequestMenuItemDTO requestItem, Optional<MenuItem> existingItem) {
        if (existingItem.isPresent()) {
            MenuItem item = existingItem.get();
            if (isPreparedReservationItem(item)) {
                handlePreparedItem(reservation, requestItem, item);
            } else {
                handlePendingOrPreparingItem(reservation, requestItem, item);
            }
        } else if (requestItem.getQuantity() > 0) {
            addNewReservationItem(reservation, requestItem);
        }
    }

    private boolean isPreparedReservationItem(MenuItem item) {
        return EnumSet.of(MenuItemStatus.PREPARED, MenuItemStatus.SERVED, MenuItemStatus.CANCELED).contains(item.getStatus());
    }

    private void handlePreparedItem(Reservation reservation, RequestMenuItemDTO requestItem, MenuItem existingItem) {
        if (requestItem.getQuantity() > existingItem.getQuantity()) {
            int additionalQuantity = requestItem.getQuantity() - existingItem.getQuantity();
            validateAvailability(requestItem.getProductId(), additionalQuantity);
            addNewReservationItem(reservation, requestItem, additionalQuantity);
        }
    }

    private void handlePendingOrPreparingItem(Reservation reservation, RequestMenuItemDTO requestItem, MenuItem item) {
        if (requestItem.getQuantity().equals(item.getQuantity())) {
            item.setSpecialInstruction(requestItem.getSpecialInstruction());
        } else if (requestItem.getQuantity() == 0) {
            reservation.getMenuItems().remove(item);
        } else {
            int quantityDelta = requestItem.getQuantity() - item.getQuantity();
            if (quantityDelta > 0) {
                validateAvailability(requestItem.getProductId(), quantityDelta);
            }
            item.setQuantity(requestItem.getQuantity());
            item.setSpecialInstruction(requestItem.getSpecialInstruction());
        }
    }

    private void addNewReservationItem(Reservation reservation, RequestMenuItemDTO requestItem) {
        addNewReservationItem(reservation, requestItem, requestItem.getQuantity());
    }

    private void addNewReservationItem(Reservation reservation, RequestMenuItemDTO requestItem, int quantity) {
        validateAvailability(requestItem.getProductId(), quantity);

        Product product = productReaderService.getProductEntity(requestItem.getProductId());

        MenuItem newDetail = MenuItem.builder()
                .reservation(reservation)
                .product(product)
                .price(product.getUnitPrice())
                .quantity(quantity)
                .status(MenuItemStatus.PENDING)
                .specialInstruction(requestItem.getSpecialInstruction())
                .build();
        reservation.getMenuItems().add(newDetail);
    }

    private void validateAvailability(long productId, int quantity) {
        Product product = productReaderService.getProductEntity(productId);
        if (product.getUnitsInStock() < quantity) {
            throw new IllegalArgumentException("Insufficient availability for menu item ID: " + productId);
        }
    }

    private void updateReservationStatus(Reservation reservation) {
        if (!ReservationStatus.COMPLETED.equals(reservation.getStatus())) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }
    }

    private void updateReservationFinancials(Reservation reservation) {
        reservation.setTotalPrice(PriceUtils.calculateReservationTotalPrice(reservation.getMenuItems()));
        reservation.getPayment().setAmount(reservation.getTotalPrice());
    }

    @Override
    @Transactional
    public ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
        reservation.setStatus(status);
        Reservation updatedOrder = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updatedOrder);
    }

    @Override
    @Transactional
    public ReservationDTO updateReservationStatusByCustomer(String username, String reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findByReservationIdAndCustomerProfileUserUsername(reservationId, username)
                .orElseThrow(()-> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
        reservation.setStatus(status);
        Reservation updatedOrder = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updatedOrder);
    }

    @Override
    @Transactional
    public ReservationDTO updateReservationStatusByEmployee(String username, String reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findByReservationIdAndEmployeeProfileUserUsername(reservationId, username)
                .orElseThrow(()-> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
        reservation.setStatus(status);
        Reservation updatedOrder = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updatedOrder);
    }

}
