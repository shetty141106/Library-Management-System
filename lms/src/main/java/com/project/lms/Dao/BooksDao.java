package com.project.lms.Dao;

import com.project.lms.Entity.Authentication;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Utility.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BooksDao  {

    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    public Books save(Books book) {
        Session s=sf.openSession();
        boolean flag = false;
        Transaction tr=s.beginTransaction();
        try {

            s.persist(book);
            flag=true;
        }
        catch (HibernateException e) {
             e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (flag == true) {
                tr.commit();
            } else {
                tr.rollback();
            }

            s.close();

        }
            return book;

    }

    public List<Books> findAll() {
        Session s=sf.openSession();

        List <Books> books=null;
        try{
           books= s.createQuery("from com.project.lms.Entity.Books ", Books.class).list();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            s.close();
        }


        return books;

    }

    public Optional<Books> findById(String isbn) {
        Session s=sf.openSession();
        Books book=null;
        try{
            book = s.find(Books.class,isbn);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       finally {
            s.close();
        }
        return Optional.ofNullable(book);
    }

    public void deleteById(String isbn) {
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();
        try {
            Books book = s.find(Books.class, isbn);
            if (book == null) {
                System.out.println("No data found");
                tr.rollback();
            }
            else {
                s.remove(book);
                tr.commit();
            }
        }

        catch (Exception e) {
            tr.rollback();
            e.printStackTrace();
        }
        finally {
            s.close();
        }
    }

    public Books update(Books existingBook) {
       Session s= sf.openSession();
       Transaction tr=s.beginTransaction();
       try{
           s.merge(existingBook);
           tr.commit();
       } catch (Exception e) {
           e.printStackTrace();
           tr.rollback();
           return null;
       }
       finally {
           s.close();
       }
       return existingBook;
    }
}