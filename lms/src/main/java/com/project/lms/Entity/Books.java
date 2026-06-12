package com.project.lms.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books {

    @Id
    private String isbn;

    private String title;

    private int edition;

    private String authName;

    private double price;

    private String category;
}