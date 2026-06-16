package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationRequest {
    private Long userid;
    private String isbn;
    private String staff;
    private LocalDate returnDate;
}
