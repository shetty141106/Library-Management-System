package com.project.lms.Dao;

import com.project.lms.Entity.*;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<RefreshToken> findByToken(String refreshTokenString) {
        try (Session session = sf.openSession()) {
            String hql = "FROM RefreshToken rt WHERE rt.token = :tokenParam";
            RefreshToken foundToken = session.createQuery(hql, RefreshToken.class)
                    .setParameter("tokenParam", refreshTokenString)
                    .uniqueResult();
            return Optional.ofNullable(foundToken);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
