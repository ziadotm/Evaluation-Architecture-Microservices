package com.example.service;


import com.example.dao.IDao;
import com.example.classes.EmployeTache;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeTacheService implements IDao<EmployeTache> {

    @Override
    public boolean create(EmployeTache et) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.persist(et);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(EmployeTache et) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.merge(et);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(EmployeTache et) {
        Transaction t = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            t = session.beginTransaction();
            session.remove(et);
            t.commit();
            return true;
        } catch (Exception ex) {
            if (t != null) t.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public EmployeTache findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(EmployeTache.class, id);
        }
    }

    @Override
    public List<EmployeTache> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from EmployeTache", EmployeTache.class).list();
        }
    }
}
