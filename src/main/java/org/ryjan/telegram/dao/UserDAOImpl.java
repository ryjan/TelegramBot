package org.ryjan.telegram.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.utils.HibernateSessionFactory;

public class UserDAOImpl implements UserDAO {

    @Override
    public UserDatabase findById(long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(UserDatabase.class, id);
    }

    @Override
    public BankDatabase findBankById(long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(BankDatabase.class, id);
    }

    @Override
    public void save(UserDatabase userDatabase) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(userDatabase);
        tx.commit();
        session.close();
    }


    @Override
    public void update(UserDatabase userDatabase) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(userDatabase);
        tx.commit();
        session.close();
    }

    @Override
    public void delete(UserDatabase userDatabase) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.remove(userDatabase);
        tx.commit();
        session.close();
    }
}
