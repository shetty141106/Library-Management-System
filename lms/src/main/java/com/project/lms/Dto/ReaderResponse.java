package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReaderResponse {
    private String name;
    private String email;
    private String password;
    private String address;
    private List<String> phones;
}
