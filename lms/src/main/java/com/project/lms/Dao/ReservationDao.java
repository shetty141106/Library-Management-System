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

    public void save(Reservation reservation) {
        Session session=sf.openSession();
        Transaction tr= session.beginTransaction();
        try {
            session.merge(reservation);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
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
