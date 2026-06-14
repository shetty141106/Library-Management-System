package com.project.lms.Service;

import com.project.lms.Dao.ReportDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReportResponse;
import com.project.lms.Entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    private ReportResponse toResponse(Report report) {

        return new ReportResponse(
                report.getReportId(),
                report.getReportType(),
                report.getReservation() != null
                        ? report.getReservation().getReservationId()
                        : null,
                report.getStaff() != null
                        ? report.getStaff().getStaff_id()
                        : null
        );
    }

    public ApiResponse<List<ReportResponse>> getAllReports() {

        List<Report> reports = reportDao.findAll();

        if (reports.isEmpty())
            return ApiResponse.fail("No reports found.");

        List<ReportResponse> res =
                reports.stream()
                        .map(this::toResponse)
                        .toList();

        return ApiResponse.ok("All reports fetched.", res);
    }

    public ApiResponse<ReportResponse> getReportById(Long id) {

        Optional<Report> reportOptional =
                reportDao.findById(id);

        if (reportOptional.isEmpty())
            return ApiResponse.fail("Report not found.");

        return ApiResponse.ok(
                "Report fetched.",
                toResponse(reportOptional.get())
        );
    }

    public ApiResponse<ReportResponse> addReport(Report report) {

        reportDao.save(report);

        return ApiResponse.ok(
                "Report created.",
                toResponse(report)
        );
    }

    public ApiResponse<Void> deleteReport(Long id) {

        Optional<Report> reportOptional =
                reportDao.findById(id);

        if (reportOptional.isEmpty())
            return ApiResponse.fail("Report not found.");

        reportDao.deleteById(id);

        return ApiResponse.ok(
                "Report deleted.",
                null
        );
    }
}