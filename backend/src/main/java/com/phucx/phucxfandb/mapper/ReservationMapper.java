package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class})
public interface ReservationMapper {

    @Mapping(target = "customer.profile.user.roles", ignore = true)
    ReservationDTO toReservationDTO(Reservation reservation);


    @Mapping(target = "menuItems", qualifiedByName = {"toMenuItemDTO"})
    @Mapping(target = "customer.profile.user.roles", ignore = true)
    @Mapping(target = "employee.profile.user.roles", ignore = true)
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
    @Mapping(target = "notes", source = "reservationDTO.notes")
    Reservation toEmployeeReservation(RequestReservationDTO reservationDTO, ReservationTable table, Employee employee);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "table", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateReservation(RequestReservationDTO requestReservationDTO, @MappingTarget Reservation reservation);

}
