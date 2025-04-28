package com.phucx.phucxfandb.service.feedback.impl;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.request.RequestFeedBackDTO;
import com.phucx.phucxfandb.dto.response.FeedBackDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Feedback;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.FeedbackMapper;
import com.phucx.phucxfandb.repository.FeedbackRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.feedback.FeedBackUpdateService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedBackUpdateServiceImpl implements FeedBackUpdateService {
    private final ReservationReaderService reservationReaderService;
    private final OrderReaderService orderReaderService;
    private final CustomerReaderService customerReaderService;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper mapper;

    @Override
    @Modifying
    @Transactional
    public FeedBackDTO createFeedbackForOrder(String username, String orderId, RequestFeedBackDTO requestFeedBackDTO) {
        log.info("createFeedbackForOrder(username={}, orderId={}, requestFeedBackDTO={})",
                username, orderId, requestFeedBackDTO);
        Order order = orderReaderService.getOrderEntity(orderId);

        Customer customer = customerReaderService.getCustomerEntityById(requestFeedBackDTO.getCustomerId());

        Feedback feedback = mapper.toFeedback(requestFeedBackDTO, order, customer);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return mapper.toFeedbackDTO(updatedFeedback);
    }

    @Override
    @Modifying
    @Transactional
    public FeedBackDTO createFeedbackForReservation(String username, String reservationId,
            RequestFeedBackDTO requestFeedBackDTO) {
        log.info("createFeedbackForReservation(username={}, reservationId={}, requestFeedBackDTO={})",
                username, reservationId, requestFeedBackDTO);
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);

        if (!ReservationStatus.COMPLETE.equals(reservation.getStatus())) {
            throw new IllegalStateException(
                    "Feedback can only be submitted for completed reservations with served pre-orders");
        }
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);

        Feedback feedback = mapper.toFeedback(requestFeedBackDTO, reservation, customer);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return mapper.toFeedbackDTO(updatedFeedback);
    }

    @Override
    @Modifying
    @Transactional
    public FeedBackDTO updateFeedback(String username, Long id, RequestFeedBackDTO requestFeedBackDTO) {
        log.info("updateFeedback(id={}, requestFeedBackDTO={})", id, requestFeedBackDTO);
        Feedback feedback = feedbackRepository.findByIdAndCustomerProfileUserUsernameAndIsDeletedFalse(id, username)
                .orElseThrow(() -> new NotFoundException("Feedback", "user", username));

        mapper.updateFeedback(requestFeedBackDTO, feedback);
        feedbackRepository.save(feedback);
        return mapper.toFeedbackDTO(feedback);
    }

    @Override
    @Modifying
    @Transactional
    public void deleteFeedback(String username, Long id) {
        log.info("deleteFeedback(username={}, id={})", username, id);
        Feedback feedback = feedbackRepository.findByIdAndCustomerProfileUserUsernameAndIsDeletedFalse(id, username)
                .orElseThrow(() -> new NotFoundException("Feedback", "user", username));
        feedback.setIsDeleted(Boolean.TRUE);
        feedbackRepository.save(feedback);
    }
}
