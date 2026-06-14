package com.project.lms.Dto;

import com.project.lms.Entity.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponse {

    private Long reportId;
    private Integer reservationId;
    private Integer staffId;
}