package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReservationRequest;
import com.project.lms.Dto.ReservationResponse;
import com.project.lms.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    private ReservationService resService;

    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<ReservationResponse>> issueBook(@RequestBody ReservationRequest req){
        ApiResponse<ReservationResponse> res = resService.issueBook(req);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/return/{res_id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> returnBook(@PathVariable Long res_id){
        ApiResponse<ReservationResponse> res = resService.returnBook(res_id);
        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}
