package com.project.lms.Controller;

import com.project.lms.Entity.Reader;
import com.project.lms.Service.ReaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping
    public List<Reader> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable int id) {
        return readerService.getReaderById(id);
    }

    @PostMapping
    public Reader addReader(@RequestBody Reader reader) {
        return readerService.addReader(reader);
    }

    @PutMapping("/{id}")
    public Reader updateReader(@PathVariable int id,
                               @RequestBody Reader reader) {
        return readerService.updateReader(id, reader);
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable int id) {
        readerService.deleteReader(id);
        return "Reader deleted successfully";
    }
}