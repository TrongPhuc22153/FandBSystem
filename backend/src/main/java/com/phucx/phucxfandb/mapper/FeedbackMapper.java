package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestFeedBackDTO;
import com.phucx.phucxfandb.dto.response.FeedBackDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Feedback;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Feedback toFeedback(RequestFeedBackDTO feedBackDTO, Order order, Customer customer);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "reservation", source = "reservation")
    Feedback toFeedback(RequestFeedBackDTO feedBackDTO, Reservation reservation, Customer customer);

    @Mapping(target = "order.customer", ignore = true)
    @Mapping(target = "order.employee", ignore = true)
    @Mapping(target = "order.customer.profile.user.roles", ignore = true)
    @Mapping(target = "customer.profile.user.roles", ignore = true)
    FeedBackDTO toFeedbackDTO(Feedback feedback);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    void updateFeedback(RequestFeedBackDTO requestFeedBackDTO, @MappingTarget Feedback feedback);
}
