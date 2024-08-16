package org.ryjan.telegram.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ryjan.telegram.database.Bank;
import org.ryjan.telegram.database.User;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.utils.HibernateSessionFactory;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);
    }

    @Override
    public Bank findBankById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(Bank.class, id);
    }

    @Override
    public void save(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
        session.close();
    }

    @Override
    public void update(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(user);
        tx.commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(user);
        tx.commit();
        session.close();
    }
}
