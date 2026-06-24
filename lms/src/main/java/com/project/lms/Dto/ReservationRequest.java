package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ReservationRequest {
    private Long readerId;
    private String readerName;
    private List<String> readerPhones;
    private String isbn;
    private Long staffId;
}
