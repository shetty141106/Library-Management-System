package com.project.lms.Utility;

import com.project.lms.Entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration c = new Configuration();

            // Add all your entity classes here as your project grows
            c.addAnnotatedClass(Authentication.class);
            c.addAnnotatedClass(Reader.class);
            c.addAnnotatedClass(Books.class);
            c.addAnnotatedClass(Publisher.class);
            c.addAnnotatedClass(Reservation.class);
            c.addAnnotatedClass(Staff.class);
            c.addAnnotatedClass(RefreshToken.class);

            sessionFactory = c.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}