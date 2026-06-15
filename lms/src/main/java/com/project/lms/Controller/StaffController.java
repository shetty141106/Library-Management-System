package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.StaffResponse;
import com.project.lms.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaff() {

        ApiResponse<List<StaffResponse>> res =
                staffService.getAllStaff();

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>>
    getStaffById(@PathVariable int id) {

        ApiResponse<StaffResponse> res =
                staffService.getStaffById(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>>
    addStaff(@RequestBody StaffResponse response) {

        ApiResponse<StaffResponse> res =
                staffService.addStaff(response);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>>
    updateStaff(
            @PathVariable int id,
            @RequestBody StaffResponse response) {

        ApiResponse<StaffResponse> res =
                staffService.updateStaff(id, response);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteStaff(@PathVariable int id) {

        ApiResponse<Void> res =
                staffService.deleteStaff(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }


}