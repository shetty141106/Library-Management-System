package com.project.lms.Dao;

import com.project.lms.Entity.*;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public Reservation save(Reservation reservation) {
        Session session=sf.openSession();
        Transaction tr= session.beginTransaction();
        try {
            Reservation savedReservation = session.merge(reservation);
            tr.commit();
            return savedReservation;
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
            return null;
        }
    }

    public boolean isBookAlreadyIssuedToReader(String isbn, Long readerId) {
        Session session = sf.openSession();
        try {
            String hql = "SELECT count(r) FROM Reservation r " +
                    "WHERE r.book.isbn = :isbn " +
                    "AND r.reader.userId = :readerId " +
                    "AND r.reservationType = 'ISSUED'";

            Long count = session.createQuery(hql, Long.class)
                    .setParameter("isbn", isbn)
                    .setParameter("readerId", readerId)
                    .uniqueResult();

            return count != null && count > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public void deleteById(Long resid) {
        Session session=sf.openSession();
        Transaction tr= session.beginTransaction();
        try{
            Reservation reservation=session.find(Reservation.class,resid);
            session.remove(reservation);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } catch (Throwable e) {
            e.printStackTrace();
            tr.rollback();
        }
        finally {
            session.close();
        }

    }

    public Optional<Reservation> findById(Long resid) {
        Session session=sf.openSession();
        Reservation reservation=null;
        try{
             reservation=session.find(Reservation.class,resid);
            return Optional.ofNullable(reservation);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        finally {
            session.close();
        }

    }
}
