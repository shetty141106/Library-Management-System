package com.project.lms.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String email;

    private String name;

    private String address;

    @ElementCollection
    private List<String> phones;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Books> books;
}


