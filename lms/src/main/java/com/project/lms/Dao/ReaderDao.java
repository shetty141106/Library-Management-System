package com.project.lms.Dao;

import com.project.lms.Entity.Reader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReaderDao {

    Configuration cfg = null;
    SessionFactory sf = null;

    public ReaderDao() {
        cfg = new Configuration();
        cfg.addAnnotatedClass(Reader.class);
        sf = cfg.buildSessionFactory();
    }


    public List<Reader> findAll() {

        Session s = sf.openSession();
        List<Reader> r = null;
        try {
            r = s.createQuery("from Reader").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return r;
    }

    public Reader findById(int id) {
        Session s = sf.openSession();
        Reader r = null;
        try {
            r = s.find(Reader.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            return r;
        }
    }

    public Reader save(Reader reader) {
        Session s = sf.openSession();
        Transaction tr = null;
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

    public void deleteById(int id) {
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