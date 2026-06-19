package com.project.lms.Entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staff_id;
    private String name;
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> phones;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    private Authentication authentication;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Reservation> reservation;
}
