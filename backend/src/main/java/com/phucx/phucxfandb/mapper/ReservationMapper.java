package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class, UserMapper.class, PaymentMapper.class})
public interface ReservationMapper {

    @Mapping(target = "payment", qualifiedByName = "toPaymentDTO")
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    ReservationDTO toReservationDTO(Reservation reservation);

    @Mapping(target = "menuItems", qualifiedByName = {"toMenuItemDTO"})
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "employee.profile.user", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "payment", ignore = true)
    ReservationDTO toReservationListEntryDTO(Reservation reservation);

    @Mapping(target = "table", source = "table")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "notes", source = "reservationDTO.notes")
    @Mapping(target = "reservationId", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Reservation toCustomerReservation(RequestReservationDTO reservationDTO, ReservationTable table, Customer customer);

    @Mapping(target = "table", source = "table")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "reservationId", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "notes", source = "reservationDTO.notes")
    Reservation toEmployeeReservation(RequestReservationDTO reservationDTO, ReservationTable table, Employee employee);
}
