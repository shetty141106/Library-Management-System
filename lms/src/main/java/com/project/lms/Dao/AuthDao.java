package com.project.lms.Dao;

import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthDao {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public void save(Authentication auth) {

        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            s.persist(auth);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
            throw new RuntimeException("Failed to save to database: " + e.getMessage(), e);
        } finally {
            s.close();
        }

    }

    public Optional<Authentication> findByEmail(String email) {

        Session s = sf.openSession();
        Authentication authentication = null;

        try {
            String hql = "FROM Authentication a WHERE a.email = :email";

            authentication = s.createQuery(hql, Authentication.class)
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }

        return Optional.ofNullable(authentication);
    }
}
