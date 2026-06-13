package com.project.lms.Entity;

import com.project.lms.Entity.ReportType;
import com.project.lms.Entity.Reservation;
import com.project.lms.Entity.Staff;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
}