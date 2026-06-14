package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReaderResponse {
    private String name;
    private String email;
    private String address;
    private List<String> phones;
    private String accessToken;
    private String refreshToken;
    private String username;
}
