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
public class Publisher {

    @Id
    private int publisher_id;
    private int yearOfPublication;
    private String name;

    @OneToMany
    private List<Books> books;

}