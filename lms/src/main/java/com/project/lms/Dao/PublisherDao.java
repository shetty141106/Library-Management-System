package com.project.lms.Dao;

import com.project.lms.Entity.*;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PublisherDao {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public List<Publisher> findAll() {
        Session s = sf.openSession();

        List<Publisher> publisher = null;
        try {
            publisher = s.createQuery("from com.project.lms.Entity.Publisher ", Publisher.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return publisher;
    }

    public Optional<Publisher> findById(Integer id) {

        Session session = sf.openSession();
        Publisher publisher = null;
        try {
            publisher = session.find(Publisher.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Optional.ofNullable(publisher);
    }

    public Publisher save(Publisher publisher) {
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            s.persist(publisher);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            s.close();
        }
        return publisher;
    }

    public void deleteById(Integer id) {
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            s.remove(Publisher.class);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            s.close();
        }
    }
    public Optional<Publisher> findByName(String publisherName) {
        Session s = sf.openSession();

        try {
            Query<Publisher> query = s.createQuery(
                    "FROM Publisher p WHERE p.name = :name",
                    Publisher.class);

            query.setParameter("name", publisherName);

            Publisher publisher = query.uniqueResult();

            return Optional.ofNullable(publisher);

        } finally {
            s.close();
        }
    }
    }

