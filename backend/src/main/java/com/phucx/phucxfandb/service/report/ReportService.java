package com.phucx.phucxfandb.service.report;

import com.phucx.phucxfandb.dto.response.AnalyticDTO;
import com.phucx.phucxfandb.dto.response.ReportDTO;

import java.time.LocalDate;

public interface ReportService {
    ReportDTO getReport(LocalDate startDate, LocalDate endDate);

    AnalyticDTO getMetrics(LocalDate startDate, LocalDate endDate);

}
