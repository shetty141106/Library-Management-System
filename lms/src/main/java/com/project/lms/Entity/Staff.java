package com.project.lms.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Staff {
    @Id
    private int staff_id;
    private String name;

    @OneToOne
    private Authentication authentication;

    @OneToMany
    private Reservation reservation;
}
