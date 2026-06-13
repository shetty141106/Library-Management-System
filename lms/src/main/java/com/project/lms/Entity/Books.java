package com.project.lms.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    private int quantity;

    @ManyToOne
    private Books books;

    @ManyToOne
    private Reader reader;
}