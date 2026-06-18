package com.project.lms.Utility;

import com.project.lms.Entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration c = new Configuration();

            // Database configuration credentials defined ONCE
            c.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            c.setProperty("hibernate.connection.url", "jdbc:mysql://49t2jbFnL9xeerB.root:<PASSWORD>@gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000/lms-db");
            c.setProperty("hibernate.connection.username", "49t2jbFnL9xeerB.root");
            c.setProperty("hibernate.connection.password", "nq9S5alKASRxDd5B");
            c.setProperty("hibernate.hbm2ddl.auto", "update");
            c.setProperty("hibernate.show_sql", "true");
            c.setProperty("hibernate.format_sql", "true");

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