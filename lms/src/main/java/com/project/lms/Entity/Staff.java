package com.project.lms.Entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Staff {
    @Id
    private Long staff_id;
    private String name;
    private String address;

    @OneToOne
    private Authentication authentication;

    @OneToMany
    private List<Reservation> reservation;
}
