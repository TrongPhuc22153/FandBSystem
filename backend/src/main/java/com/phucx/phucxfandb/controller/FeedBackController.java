package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestFeedBackDTO;
import com.phucx.phucxfandb.dto.response.FeedBackDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.feedback.FeedBackReaderService;
import com.phucx.phucxfandb.service.feedback.FeedBackUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Feedback API", description = "Customer feedback endpoint")
@RequestMapping(value = "/api/v1/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedBackController {

    private final FeedBackUpdateService feedbackUpdateService;
    private final FeedBackReaderService feedbackReadService;

    @PostMapping(value = "/reservation/{reservationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Submit reservation user feedback", description = "Allows authenticated users to submit reservation feedback.")
    public ResponseEntity<ResponseDTO<FeedBackDTO>> submitReservationFeedback(
            Principal principal,
            @PathVariable String reservationId,
            @Valid @RequestBody RequestFeedBackDTO requestFeedbackDTO
    ) {
        FeedBackDTO feedbackDTO = feedbackUpdateService.createFeedbackForReservation(principal.getName(), reservationId, requestFeedbackDTO);
        ResponseDTO<FeedBackDTO> response = ResponseDTO.<FeedBackDTO>builder()
                .message("Feedback submitted successfully")
                .data(feedbackDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/order/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Submit order user feedback", description = "Allows authenticated users to submit order feedback.")
    public ResponseEntity<ResponseDTO<FeedBackDTO>> submitOrderFeedback(
            Principal principal,
            @PathVariable String orderId,
            @Valid @RequestBody RequestFeedBackDTO requestFeedbackDTO
    ) {
        FeedBackDTO feedbackDTO = feedbackUpdateService.createFeedbackForOrder(principal.getName(), orderId, requestFeedbackDTO);
        ResponseDTO<FeedBackDTO> response = ResponseDTO.<FeedBackDTO>builder()
                .message("Feedback submitted successfully")
                .data(feedbackDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get user's own feedback", description = "Allows authenticated users to retrieve their submitted feedback.")
    public ResponseEntity<Page<FeedBackDTO>> getMyFeedback(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<FeedBackDTO> feedbackList = feedbackReadService.getFeedbackByUsername(principal.getName(), pageNumber, pageSize);
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/{feedbackId}")
    @Operation(summary = "Get feedback by ID", description = "Allows authorized users to retrieve specific feedback by its ID.")
    public ResponseEntity<FeedBackDTO> getFeedbackById(@PathVariable Long feedbackId) {
        FeedBackDTO feedbackDTO = feedbackReadService.getFeedbackById(feedbackId);
        return ResponseEntity.ok(feedbackDTO);
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "Get all reservation feedback", description = "Allows authorized users to retrieve all submitted feedback.")
    public ResponseEntity<Page<FeedBackDTO>> getAllReservationFeedback(
            @PathVariable String reservationId,
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<FeedBackDTO> allFeedback = feedbackReadService.getFeedbackByReservationId(reservationId, pageNumber, pageSize);
        return ResponseEntity.ok(allFeedback);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all order feedback", description = "Allows authorized users to retrieve all submitted feedback.")
    public ResponseEntity<Page<FeedBackDTO>> getAllOrderFeedback(
            @PathVariable String orderId,
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<FeedBackDTO> allFeedback = feedbackReadService.getFeedbackByOrderId(orderId, pageNumber, pageSize);
        return ResponseEntity.ok(allFeedback);
    }
}
