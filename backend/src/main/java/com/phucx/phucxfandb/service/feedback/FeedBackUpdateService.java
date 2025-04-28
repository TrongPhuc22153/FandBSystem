package com.phucx.phucxfandb.service.feedback;

import com.phucx.phucxfandb.dto.request.RequestFeedBackDTO;
import com.phucx.phucxfandb.dto.response.FeedBackDTO;

public interface FeedBackUpdateService {
    FeedBackDTO createFeedbackForOrder(String username, String orderId, RequestFeedBackDTO requestFeedBackDTO);
    FeedBackDTO createFeedbackForReservation(String username, String reservationId, RequestFeedBackDTO requestFeedBackDTO);

    FeedBackDTO updateFeedback(String username, Long id, RequestFeedBackDTO requestFeedBackDTO);
    void deleteFeedback(String username, Long id);
}
