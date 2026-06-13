package com.project.lms.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Staff {
    @Id
    private int staff_id;
    private Staff name;

    @OneToMany(mappedBy = "staff")
    private List<Report> reports = new ArrayList<>();
}
