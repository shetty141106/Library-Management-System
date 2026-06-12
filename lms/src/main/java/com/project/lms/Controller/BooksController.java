package com.project.lms.Controller;


import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.BooksRequest;
import com.project.lms.Dto.BooksResponse;
import com.project.lms.Entity.Books;
import com.project.lms.Service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksService bookService;

    @Autowired
    private ApiResponse apiResponse;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BooksResponse>>> getAllBooks() {
        ApiResponse<List<BooksResponse>> res = bookService.getAllBooks();
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<ApiResponse<BooksResponse>> getBookByIsbn(@PathVariable String isbn) {
        ApiResponse<BooksResponse> res = bookService.getBookByIsbn(isbn);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BooksResponse>> addBook(@RequestBody BooksRequest bookreq) {
        ApiResponse<BooksResponse> res = bookService.addBook(bookreq);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<ApiResponse<BooksResponse>> updateBook(@PathVariable String isbn,
                           @RequestBody BooksRequest bookreq) {
        ApiResponse<BooksResponse> res = bookService.updateBook(isbn, bookreq);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable String isbn) {
        ApiResponse<Void> res = bookService.deleteBook(isbn);
        return res.isSuccess()? ResponseEntity.ok(res):ResponseEntity.badRequest().body(res);
    }
}