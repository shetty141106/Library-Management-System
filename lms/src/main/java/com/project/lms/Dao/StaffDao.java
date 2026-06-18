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
public class StaffDao {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public List<Staff> findAll() {

        Session s = sf.openSession();

        try {
            return s.createQuery(
                    "from com.project.lms.Entity.Staff",
                    Staff.class
            ).list();
        }
        finally {
            s.close();
        }
    }

    public Optional<Staff> findById(int id) {

        Session s = sf.openSession();

        try {
            return Optional.ofNullable(
                    s.find(Staff.class, id)
            );
        }
        finally {
            s.close();
        }
    }

    public Staff save(Staff staff) {

        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        try {
            s.saveOrUpdate(staff);
            tr.commit();
            return staff;
        }
        catch (Exception e) {
            tr.rollback();
            throw e;
        }
        finally {
            s.close();
        }
    }

    public void deleteById(int id) {

        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        try {

            Staff staff = s.find(Staff.class, id);

            if (staff != null) {
                s.remove(staff);
                tr.commit();
            }
            else {
                tr.rollback();
            }

        } finally {
            s.close();
        }
    }
}