package com.project.lms.Dto;

import com.project.lms.Entity.Books;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PublisherResponse {
    private int publisher_id;
    private int yearOfPublication;
    private String name;
    private List<Books> books;
}
