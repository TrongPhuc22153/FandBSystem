package com.phucx.phucxfandb.service.feedback;

import com.phucx.phucxfandb.dto.response.FeedBackDTO;
import org.springframework.data.domain.Page;

public interface FeedBackReaderService {

    Page<FeedBackDTO> getFeedbacks(int pageNumber, int pageSize);


    FeedBackDTO getFeedbackById(Long id);

    Page<FeedBackDTO> getFeedbackByOrderId(String orderId, int pageNumber, int pageSize);


    Page<FeedBackDTO> getFeedbackByReservationId(String reservationId, int pageNumber, int pageSize);

    Page<FeedBackDTO> getFeedbackByUsername(String username, int pageNumber, int pageSize);
}
