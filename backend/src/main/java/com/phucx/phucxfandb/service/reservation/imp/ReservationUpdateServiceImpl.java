package com.phucx.phucxfandb.service.reservation.imp;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.constant.TableStatus;
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
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationUpdateServiceImpl implements ReservationUpdateService {
    private final CustomerReaderService customerReaderService;
    private final EmployeeReaderService employeeReaderService;
    private final ReservationRepository reservationRepository;
    private final ReservationTableReaderService reservationTableReaderService;
    private final ReservationTableUpdateService reservationTableUpdateService;
    private final ProductReaderService productReaderService;
    private final ProductUpdateService productUpdateService;
    private final ReservationMapper reservationMapper;
    private final MenuItemMapper menuItemMapper;

    @Override
    @Modifying
    @Transactional
    public ReservationDTO createCustomerReservation(String username, RequestReservationDTO requestReservationDTO) {
        log.info("createCustomerReservation(username={}, requestReservationDTO={})", username, requestReservationDTO);
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);
        ReservationTable table = reservationTableReaderService
                .getReservationTableEntity(requestReservationDTO.getTableId());

        Reservation newReservation = reservationMapper.toCustomerReservation(
                requestReservationDTO,
                table,
                customer);
        newReservation.setStatus(ReservationStatus.PENDING);

        requestReservationDTO.getMenuItems().forEach(requestMenuItem -> {
            Product product = productReaderService.getProductEntity(requestMenuItem.getProductId());

            if(requestMenuItem.getQuantity()>product.getUnitsInStock()){
                throw new IllegalArgumentException("Insufficient stock for product ID: " + requestMenuItem.getProductId());
            }
            Product updated = productUpdateService.updateProductInStock(requestMenuItem.getProductId(), requestMenuItem.getQuantity());
            MenuItem newMenuItem = menuItemMapper.toMenuItem(requestMenuItem, newReservation, updated);
            newReservation.getMenuItems().add(newMenuItem);
        });

        Reservation saved = reservationRepository.save(newReservation);
        return reservationMapper.toReservationDTO(saved);
    }

    @Override
    @Modifying
    @Transactional
    public ReservationDTO createEmployeeReservation(String username, RequestReservationDTO requestReservationDTO) {
        log.info("createEmployeeReservation(username={}, requestReservationDTO={})", username, requestReservationDTO);
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);
        ReservationTable table = reservationTableReaderService
                .getReservationTableEntity(requestReservationDTO.getTableId());


        Reservation newReservation = reservationMapper.toEmployeeReservation(
                requestReservationDTO,
                table,
                employee);
        newReservation.setStatus(ReservationStatus.PENDING);

        List<MenuItem> newMenuItems = new ArrayList<>();
        requestReservationDTO.getMenuItems().forEach(requestMenuItem -> {
            Product product = productReaderService.getProductEntity(requestMenuItem.getProductId());

            if(requestMenuItem.getQuantity()>product.getUnitsInStock()){
                throw new IllegalArgumentException("Insufficient stock for product ID: " + requestMenuItem.getProductId());
            }
            Product updatedProduct = productUpdateService.updateProductInStock(requestMenuItem.getProductId(), requestMenuItem.getQuantity());
            MenuItem newMenuItem = menuItemMapper.toMenuItem(requestMenuItem, newReservation, updatedProduct);
            newMenuItems.add(newMenuItem);
        });

        newReservation.setMenuItems(newMenuItems);

        Reservation saved = reservationRepository.save(newReservation);
        return reservationMapper.toReservationDTO(saved);
    }


    @Override
    @Modifying
    @Transactional
    public ReservationDTO updateReservation(String username, String reservationId,
            RequestReservationDTO reservationDTO) {
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status) {
        log.info("updateReservationStatus(reservationId={}, status={})", reservationId, status);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new NotFoundException("Reservation", "id", reservationId));
        reservation.setStatus(status);
        Reservation updatedOrder = reservationRepository.save(reservation);
        return reservationMapper.toReservationDTO(updatedOrder);
    }

    @Override
    @Modifying
    @Transactional
    public ReservationDTO createReservationAndAssignTable(String username,
            RequestReservationDTO requestReservationDTO) {
        log.info("createReservationAndAssignTable(username={}, requestReservationDTO={})", username,
                requestReservationDTO);
        Integer numberOfGuests = requestReservationDTO.getNumberOfGuests();
        LocalDateTime startTime = requestReservationDTO.getStartTime();
        LocalDateTime endTime = requestReservationDTO.getEndTime();

        List<ReservationTable> availableTables = reservationTableReaderService
                .getTablesEntities(TableStatus.UNOCCUPIED, numberOfGuests, startTime, endTime);

        Optional<ReservationTable> assignedTable = availableTables.stream()
                // Add your prioritization logic here if needed
                .findFirst();

        if (assignedTable.isPresent()) {
            ReservationTable tableToAssign = assignedTable.get();
            requestReservationDTO.setTableId(tableToAssign.getTableId());
            requestReservationDTO.setStatus(ReservationStatus.CONFIRMED); // Set initial reservation status

            reservationTableUpdateService.updateTableStatus(
                    tableToAssign.getTableId(),
                    TableStatus.RESERVED);

            Customer customer = customerReaderService.getCustomerEntityByUsername(username);

            Reservation newReservation = reservationMapper.toCustomerReservation(
                    requestReservationDTO,
                    tableToAssign,
                    customer);

            Reservation created = reservationRepository.save(newReservation);
            return reservationMapper.toReservationDTO(created);
        } else {
            throw new NotFoundException("No suitable table available for the requested time and party size.");
        }
    }
}
