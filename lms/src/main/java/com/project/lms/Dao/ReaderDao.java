package com.project.lms.Dao;

import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Utility.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReaderDao {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public List<Reader> findAll() {

        Session s = sf.openSession();
        List<Reader> r = null;
        try {
            r = s.createQuery("from com.project.lms.Entity.Reader", Reader.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return r;
    }

    public Optional<Reader> findById(Long id) {
        Session s = sf.openSession();
        Reader r = null;
        try {
            r = s.find(Reader.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            s.close();
        }
        return Optional.ofNullable(r);
    }

    public Reader save(Reader reader) {
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            s.persist(reader);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            s.close();
        }
        return reader;
    }

    public void deleteById(Long id) {
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            Reader r = s.find(Reader.class, id);
            if (r == null) {
                System.out.println("no data found");
                tr.rollback();
            } else {
                s.remove(r);
                tr.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}