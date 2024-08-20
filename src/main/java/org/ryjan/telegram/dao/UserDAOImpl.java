package org.ryjan.telegram.dao;

import org.apache.catalina.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.utils.HibernateSessionFactory;
import org.ryjan.telegram.utils.UserGroup;

public class UserDAOImpl implements UserDAO {

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
    public boolean isOwner(long id) {
       /* Session session = HibernateSessionFactory.getSessionFactory().openSession();
        String hql = "SELECT CASE WHEN u.userGroup = 'Owner' THEN true ELSE false END " +
                "FROM UserDatabase u WHERE u.id = :id";
        Query<Boolean> query = session.createQuery(hql, Boolean.class);
        query.setParameter("id", id);
        return query.uniqueResult();
        */
        UserDatabase user = findUser(id);
        return user.getUserGroup().equals("Owner");
    }

    @Override
    public boolean isOwner(String username) {
        return false;
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
