package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReaderResponse;
import com.project.lms.Entity.Reader;
import com.project.lms.Service.ReaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReaderResponse>>> getAllReaders() {

        ApiResponse<List<ReaderResponse>> res =
                readerService.getAllReaders();

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderResponse>> getReaderById(
            @PathVariable Long id) {

        ApiResponse<ReaderResponse> res =
                readerService.getReaderById(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReaderResponse>> addReader(
            @RequestBody Reader reader) {

        ApiResponse<ReaderResponse> res =
                readerService.addReader(reader);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderResponse>> updateReader(
            @PathVariable Long id,
            @RequestBody Reader reader) {

        ApiResponse<ReaderResponse> res =
                readerService.updateReader(id, reader);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReader(
            @PathVariable Long id) {

        ApiResponse<Void> res =
                readerService.deleteReader(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}