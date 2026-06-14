package com.project.lms.Entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType reportType;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @OneToMany(mappedBy = "reservation")
    private List<Report> reports = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Books book;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;
}