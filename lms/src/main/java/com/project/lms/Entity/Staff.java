package com.project.lms.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Staff {
    @Id
    private int staff_id;
    private String name;

    @OneToMany(mappedBy = "staff")
    private List<Report> reports = new ArrayList<>();
}
