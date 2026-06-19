package com.project.lms.Entity;

import lombok.*;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    @Id
    private Long userId;

    private String name;

    private String email;

    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> phones;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<Reservation> reservation;
}


