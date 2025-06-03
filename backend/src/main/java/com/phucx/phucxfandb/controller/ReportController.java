package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.AnalyticDTO;
import com.phucx.phucxfandb.dto.response.ReportDTO;
import com.phucx.phucxfandb.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Admin operations for report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Get report", description = "Admin access")
    public ResponseEntity<ReportDTO> getReport(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate")LocalDate endDate) {
        ReportDTO report = reportService.getReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get metrics", description = "Admin access")
    public ResponseEntity<AnalyticDTO> getMetrics(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate")LocalDate endDate
    ) {
        AnalyticDTO metrics = reportService.getMetrics(startDate, endDate);
        return ResponseEntity.ok(metrics);
    }

}
