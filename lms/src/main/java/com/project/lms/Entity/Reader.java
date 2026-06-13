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

    private String address;

    @ElementCollection
    private List<String> phones;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Books> books;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private Authentication authentication;

}


