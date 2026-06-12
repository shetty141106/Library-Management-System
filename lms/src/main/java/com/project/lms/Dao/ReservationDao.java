package com.project.lms.Dao;

import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Entity.Reservation;
import org.hibernate.SessionFactory;
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
        sf= c.buildSessionFactory();
    }

}
