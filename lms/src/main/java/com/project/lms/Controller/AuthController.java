package com.project.lms.Controller;

import com.project.lms.Dao.AuthDao;
import com.project.lms.Dto.*;
import com.project.lms.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ReaderResponse>> register(@RequestBody RegisterRequest req) {
        ApiResponse<ReaderResponse> res = authService.register(req);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }
}
