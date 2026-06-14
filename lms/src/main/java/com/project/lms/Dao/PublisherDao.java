package com.project.lms.Dao;

import com.project.lms.Entity.Books;
import com.project.lms.Entity.Publisher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PublisherDao {

    Configuration cfg = null;
    SessionFactory sf = null;

    public PublisherDao(){
        this.cfg = new Configuration();
        cfg.addAnnotatedClass(Books.class);
        this.sf = cfg.buildSessionFactory();
    }

    public List<Publisher> findAll() {
        Session s=sf.openSession();

        List <Publisher> publisher =null;
        try{
            publisher = s.createQuery("from com.project.lms.Entity.Publisher ", Publisher.class).list();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            s.close();
        }
        return publisher;
    }

    public Optional<Publisher> findById(Integer id) {
    }

    public Publisher save(Publisher publisher) {
    }

    public void deleteById(Integer id) {
    }
}
