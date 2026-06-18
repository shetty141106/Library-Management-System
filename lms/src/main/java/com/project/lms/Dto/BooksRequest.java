package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BooksRequest {
    private String isbn;
    private String confirm_isbn;
    private String title;
    private int edition;
    private String authName;
    private double price;
    private String category;
    private int Quantity;
    private String publisherName;
    private int yearOfPublication;
}
