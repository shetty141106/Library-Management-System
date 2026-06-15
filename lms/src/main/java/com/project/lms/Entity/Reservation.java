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
    private Long reservationId;

    @Column(nullable = false)
    private String reservationType;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Books book;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;
}