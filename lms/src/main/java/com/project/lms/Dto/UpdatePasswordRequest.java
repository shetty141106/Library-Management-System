package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    private String email;
    private String old_password;
    private String password;
    private String confirm_password;
}
