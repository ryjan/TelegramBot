package org.ryjan.telegram.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.ryjan.telegram.model.BankDatabase;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.utils.HibernateSessionFactory;

public class UserDAOImpl implements UserDAO { // Это просто удалить, перенести отсюда все методы в репозиторий

    @Override
    public UserDatabase findUser(long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(UserDatabase.class, id);
    }

    @Override
    public UserDatabase findUser(String username) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        String hql = "FROM UserDatabase WHERE userTag = :userTag";
        Query<UserDatabase> query = session.createQuery(hql, UserDatabase.class);
        query.setParameter("userTag", username);
        return query.uniqueResult();
    }

    @Override
    public BankDatabase findBank(long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(BankDatabase.class, id);
    }

    @Override
    public BankDatabase findBank(String username) {
        return findUser(username).getBank();
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
