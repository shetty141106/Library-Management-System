package com.project.lms.Dto;

import com.project.lms.Entity.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponse {

    private Long reportId;
    private ReportType reportType;
    private Integer reservationId;
    private Integer staffId;
}