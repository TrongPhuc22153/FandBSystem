package com.phucx.phucxfandb.service.reservation.impl;

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
import java.util.List;
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
