package com.project.lms.Dao;

import com.project.lms.Entity.*;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenDao {
    private final SessionFactory sf = HibernateUtil.getSessionFactory();
    public RefreshToken save(RefreshToken refreshToken) {
        Session session=sf.openSession();
        Transaction tr= session.beginTransaction();

        try{
            session.persist(refreshToken);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return refreshToken;

    }
}
