package com.project.lms.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "isbn")
    private Books book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;
}