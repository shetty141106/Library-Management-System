package com.project.lms.Service;

import com.project.lms.Dao.AuthDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReaderResponse;
import com.project.lms.Dto.RegisterRequest;
import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    private PasswordEncoder passwordEncoder;

    public ReaderResponse toReaderResponse(Reader r){
        return new ReaderResponse(r.getName(), r.getAuthentication().getEmail(), r.getAuthentication().getPassword(), r.getAddress(), r.getPhones());
    }

    public ApiResponse<ReaderResponse> register(RegisterRequest req) {
        if(req.getName().isBlank() || req.getEmail().isBlank() || req.getAddress().isBlank() || req.getPhones().isEmpty() || req.getPassword().isBlank() || req.getConfirm_password().isBlank())
            return ApiResponse.fail("All fields are required.");
        if(!req.getPassword().equals(req.getConfirm_password()))
            return ApiResponse.fail("Password do not match.");

        Authentication auth = new Authentication();
        auth.setEmail(req.getEmail());
        auth.setPassword(passwordEncoder.encode(req.getPassword()));

        Reader reader = new Reader();
        reader.setName(req.getName());
        reader.setAddress(req.getAddress());
        reader.setPhones(req.getPhones());

        auth.setReader(reader);
        reader.setAuthentication(auth);
        authDao.save(auth);
        return ApiResponse.ok("Registration Successful.", toReaderResponse(reader));
    }
}
