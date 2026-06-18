package com.project.lms.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany
    private List<Reservation> reservation;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}