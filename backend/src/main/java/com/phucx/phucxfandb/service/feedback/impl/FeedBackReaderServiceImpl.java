package com.phucx.phucxfandb.service.feedback.impl;

import com.phucx.phucxfandb.dto.response.FeedBackDTO;
import com.phucx.phucxfandb.entity.Feedback;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.FeedbackMapper;
import com.phucx.phucxfandb.repository.FeedbackRepository;
import com.phucx.phucxfandb.service.feedback.FeedBackReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedBackReaderServiceImpl implements FeedBackReaderService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FeedBackDTO> getFeedbacks(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return feedbackRepository.findByIsDeletedFalse(pageable)
                .map(mapper::toFeedbackDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedBackDTO getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new NotFoundException("Feedback", "id", id));
        return mapper.toFeedbackDTO(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedBackDTO> getFeedbackByOrderId(String orderId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return feedbackRepository.findByOrderOrderIdAndIsDeletedFalse(orderId, pageable)
                .map(mapper::toFeedbackDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedBackDTO> getFeedbackByReservationId(String reservationId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return feedbackRepository.findByReservationReservationIdAndIsDeletedFalse(reservationId, pageable)
                .map(mapper::toFeedbackDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedBackDTO> getFeedbackByUsername(String username, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return feedbackRepository.findByCustomerProfileUserUsernameAndIsDeletedFalse(username, pageable)
                .map(mapper::toFeedbackDTO);
    }
}
