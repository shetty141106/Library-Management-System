package com.project.lms.Dao;

import com.project.lms.Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportDao {

    Configuration c;
    SessionFactory sf;

    public ReportDao() {
        c = new Configuration();
        c.addAnnotatedClass(Reservation.class);
        c.addAnnotatedClass(Reader.class);
        c.addAnnotatedClass(Books.class);
        c.addAnnotatedClass(Report.class);
        c.addAnnotatedClass(Staff.class);
        c.addAnnotatedClass(Authentication.class);
        sf = c.buildSessionFactory();
    }

    public List<Report> findAll() {

        Session s = sf.openSession();

        try {
            return s.createQuery(
                    "from com.project.lms.Entity.Report",
                    Report.class
            ).list();
        } finally {
            s.close();
        }
    }

    public Optional<Report> findById(Long id) {

        Session s = sf.openSession();

        try {
            return Optional.ofNullable(
                    s.find(Report.class, id)
            );
        } finally {
            s.close();
        }
    }

    public Report save(Report report) {

        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        try {
            s.saveOrUpdate(report);
            tr.commit();
            return report;
        } catch (Exception e) {
            tr.rollback();
            throw e;
        } finally {
            s.close();
        }
    }

    public void deleteById(Long id) {

        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        try {

            Report report = s.find(Report.class, id);

            if (report != null) {
                s.remove(report);
                tr.commit();
            } else {
                tr.rollback();
            }

        } finally {
            s.close();
        }
    }
}