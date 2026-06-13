package com.project.lms.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Publisher {

    @Id
    private int publisher_id;
    private int yearOfPublication;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Books> books;

}