package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestTopicDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.dto.response.TopicDTO;
import com.phucx.phucxfandb.service.topic.TopicReaderService;
import com.phucx.phucxfandb.service.topic.TopicUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/topics",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Topics", description = "Public and Admin operations for topics")
public class TopicController {
    private final TopicReaderService topicReaderService;
    private final TopicUpdateService topicUpdateService;

    @Operation(summary = "Get all topics", description = "Public access")
    @GetMapping
    public ResponseEntity<Page<TopicDTO>> getTopics(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<TopicDTO> data = topicReaderService.getTopics(pageNumber, pageSize);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get topic by ID or name", description = "Public access")
    @GetMapping(value = "/topic")
    public ResponseEntity<TopicDTO> getTopic(
            @Parameter(description = "Topic ID to retrieve a single topic", required = false)
            @RequestParam(name = "id", required = false) Long id,
            @Parameter(description = "Topic name to retrieve a single topic", required = false)
            @RequestParam(name = "name", required = false) String name
    ) {
        if (id != null && name != null && !name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot provide both id and name");
        }
        if (id != null) {
            TopicDTO data = topicReaderService.getTopic(id);
            return ResponseEntity.ok().body(data);
        }
        if (name != null && !name.trim().isEmpty()) {
            TopicDTO data = topicReaderService.getTopic(name);
            return ResponseEntity.ok().body(data);
        }
        throw new IllegalArgumentException("Either id or name must be provided");
    }

    @Operation(summary = "Update topic", description = "Admin access")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<TopicDTO>> updateTopic(
            @Valid @RequestBody RequestTopicDTO requestTopicDTO,
            @PathVariable Long id
    ) {
        TopicDTO updatedTopic = topicUpdateService.updateTopic(id, requestTopicDTO);
        ResponseDTO<TopicDTO> responseDTO = ResponseDTO.<TopicDTO>builder()
                .message("Topic updated successfully")
                .data(updatedTopic)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Create new topic", description = "Admin access")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<TopicDTO>> createTopic(
            @Valid @RequestBody RequestTopicDTO requestTopicDTO
    ) {
        TopicDTO newTopic = topicUpdateService.createTopic(requestTopicDTO);
        ResponseDTO<TopicDTO> responseDTO = ResponseDTO.<TopicDTO>builder()
                .message("Topic created successfully")
                .data(newTopic)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Create new topics", description = "Admin access")
    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<List<TopicDTO>>> createTopics(
            @Valid @RequestBody List<RequestTopicDTO> requestTopicDTOs
    ) {
        List<TopicDTO> newTopics = topicUpdateService.createTopics(requestTopicDTOs);
        ResponseDTO<List<TopicDTO>> responseDTO = ResponseDTO.<List<TopicDTO>>builder()
                .message("Topics created successfully")
                .data(newTopics)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}