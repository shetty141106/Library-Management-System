package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private String reservation_type;
    private Long userid;
    private String book;
    private String staff;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
