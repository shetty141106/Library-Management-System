package com.project.lms.Service;

import com.project.lms.Dao.AuthDao;
import com.project.lms.Dao.RefreshTokenDao;
import com.project.lms.Dto.*;
import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.RefreshToken;
import com.project.lms.Entity.Staff;
import com.project.lms.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RefreshToken createRefreshToken(Authentication auth) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(auth)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(7L * 24 * 60 * 60 * 1000)) // Valid for 7 days
                .revoked(false)
                .build();

        return refreshTokenDao.save(refreshToken);
    }

    private UserResponse toUserResponse (Staff staff, String accessToken){
        return UserResponse.builder()
                .id(staff.getStaff_id())
                .name(staff.getName())
                .email(staff.getAuthentication().getEmail())
                .address(staff.getAddress())
                .phones(staff.getPhones())
                .accessToken(accessToken)
                .build();
    }

    public ApiResponse<UserResponse> register(RegisterRequest req) {
        Optional<Authentication> opt = authDao.findByEmail(req.getEmail());
        if(opt.isPresent())
            return ApiResponse.fail("User already exists.");

        if(req.getName().isBlank() || req.getEmail().isBlank() || req.getAddress().isBlank() || req.getPhones().isEmpty() || req.getPassword().isBlank() || req.getConfirm_password().isBlank())
            return ApiResponse.fail("All fields are required.");
        if(!req.getPassword().equals(req.getConfirm_password()))
            return ApiResponse.fail("Password do not match.");

        Authentication auth = new Authentication();
        auth.setEmail(req.getEmail());
        auth.setPassword(passwordEncoder.encode(req.getPassword()));

        Staff staff=new Staff();
        staff.setName(req.getName());
        staff.setAddress(req.getAddress());
        staff.setPhones(req.getPhones());
        auth.setStaff(staff);
        staff.setAuthentication(auth);
        authDao.save(auth);

        String jwtToken = jwtService.generateToken(auth);
        RefreshToken refreshToken = createRefreshToken(auth);
        return ApiResponse.ok("Registration Successful.", toUserResponse(staff, jwtToken));
    }

    public ApiResponse<UserResponse> login(LoginRequest req) {
        Optional<Authentication> opt = authDao.findByEmail(req.getEmail());
        if(opt.isEmpty())
            return ApiResponse.fail("User not found.");
        Authentication auth = opt.get();

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getEmail(), req.getPassword())
            );
        }
        catch (Exception e){
            return ApiResponse.fail("Invalid email or password.");
        }
        String jwtToken = jwtService.generateToken(auth);
        RefreshToken refreshToken = createRefreshToken(auth);
        return ApiResponse.ok("Login Successful.", toUserResponse(auth.getStaff(), jwtToken));
    }

    public ApiResponse<Void> updatePassword(UpdatePasswordRequest req) {
        if(req.getEmail().isBlank())
            return ApiResponse.fail("Email is required.");
        Optional<Authentication> opt = authDao.findByEmail(req.getEmail());
        if(opt.isEmpty())
            return ApiResponse.fail("User not found.");
        Authentication auth = opt.get();
        if(!passwordEncoder.matches(auth.getPassword(), req.getOld_password()))
            return ApiResponse.fail("Enter valid password.");
        auth.setPassword(passwordEncoder.encode(req.getPassword()));
        authDao.save(auth);
        return ApiResponse.ok("Password updated.", null);
    }
}
