package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReaderDto;
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
    public ResponseEntity<ApiResponse<List<ReaderDto>>> getAllReaders() {

        ApiResponse<List<ReaderDto>> res =
                readerService.getAllReaders();

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderDto>> getReaderById(
            @PathVariable Long id) {

        ApiResponse<ReaderDto> res =
                readerService.getReaderById(id);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReaderDto>> addReader(
            @RequestBody ReaderDto reader) {

        ApiResponse<ReaderDto> res =
                readerService.addReader(reader);

        return res.isSuccess()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderDto>> updateReader(
            @PathVariable Long id,
            @RequestBody ReaderDto reader) {

        ApiResponse<ReaderDto> res =
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