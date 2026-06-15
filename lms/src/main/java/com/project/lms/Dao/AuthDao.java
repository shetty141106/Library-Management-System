package com.project.lms.Dao;

import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthDao {

    Configuration c=null;
    SessionFactory sf=null;

    public AuthDao(){
        c=new Configuration();
        c.addAnnotatedClass(Authentication.class);
        c.addAnnotatedClass(Reader.class);
        c.addAnnotatedClass(Books.class);
        sf=c.buildSessionFactory();
    }

    public void save(Authentication auth) {

    }

    public Optional<Authentication> findByEmail(String email) {

    }
}
