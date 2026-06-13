package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReportResponse;
import com.project.lms.Entity.Report;
import com.project.lms.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getAllReports() {

        ApiResponse<List<ReportResponse>> res =
                reportService.getAllReports();

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportResponse>>
    getReportById(@PathVariable Long id) {

        ApiResponse<ReportResponse> res =
                reportService.getReportById(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReportResponse>>
    addReport(@RequestBody Report report) {

        ApiResponse<ReportResponse> res =
                reportService.addReport(report);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteReport(@PathVariable Long id) {

        ApiResponse<Void> res =
                reportService.deleteReport(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}