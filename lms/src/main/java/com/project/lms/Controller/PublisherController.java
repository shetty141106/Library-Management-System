package com.project.lms.Controller;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.PublisherResponse;
import com.project.lms.Entity.Publisher;
import com.project.lms.Service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublisherResponse>>> getAllPublishers() {

        ApiResponse<List<PublisherResponse>> response =
                publisherService.getAllPublishers();

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherResponse>> getPublisherById(
            @PathVariable Integer id
    ) {

        ApiResponse<PublisherResponse> response =
                publisherService.getPublisherById(id);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublisherResponse>> addPublisher(
            @RequestBody Publisher publisher
    ) {

        ApiResponse<PublisherResponse> response =
                publisherService.addPublisher(publisher);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherResponse>> updatePublisher(
            @PathVariable Integer id,
            @RequestBody Publisher publisher
    ) {

        ApiResponse<PublisherResponse> response =
                publisherService.updatePublisher(id, publisher);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePublisher(
            @PathVariable Integer id
    ) {

        ApiResponse<Void> response =
                publisherService.deletePublisher(id);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}