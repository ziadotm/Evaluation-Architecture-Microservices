package com.example.dao;


import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class GenericDao<T> implements IDao<T> {
    private Class<T> clazz;

    public GenericDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T save(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(t);
        tx.commit();
        session.close();
        return t;
    }

    @Override
    public T update(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(t);
        tx.commit();
        session.close();
        return t;
    }

    @Override
    public void delete(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(t);
        tx.commit();
        session.close();
    }

    @Override
    public T findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T obj = session.get(clazz, id);
        session.close();
        return obj;
    }

    @Override
    public List<T> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> list = session.createQuery("from " + clazz.getSimpleName(), clazz).list();
        session.close();
        return list;
    }
}

