package com.project.lms.Dto;

import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Entity.Staff;
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
