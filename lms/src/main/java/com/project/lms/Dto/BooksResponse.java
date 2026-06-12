package com.project.lms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BooksResponse {
    private String isbn;
    private String title;
    private int edition;
    private String authName;
    private double price;
    private String category;
    private int Quantity;
}
