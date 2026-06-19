package com.project.lms.Controller;

import com.project.lms.Dto.*;
import com.project.lms.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest req) {
        ApiResponse<UserResponse> res = authService.register(req);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest req) {
        ApiResponse<UserResponse> res = authService.login(req);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody UpdatePasswordRequest req) {
        ApiResponse<Void> res = authService.updatePassword(req);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

}
