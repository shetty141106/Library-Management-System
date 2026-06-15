package com.project.lms.Dao;

import com.project.lms.Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {
    Configuration c=null;
    SessionFactory sf=null;
    public ReservationDao(){
        c=new Configuration();
        c.addAnnotatedClass(Reservation.class);
        c.addAnnotatedClass(Reader.class);
        c.addAnnotatedClass(Books.class);
        c.addAnnotatedClass(Staff.class);
        c.addAnnotatedClass(Authentication.class);
        sf= c.buildSessionFactory();
    }

    public void save(Reservation reservation) {
        Session session=sf.openSession();
        Transaction tr= session.beginTransaction();
        try {
            session.persist(reservation);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

    }
}
