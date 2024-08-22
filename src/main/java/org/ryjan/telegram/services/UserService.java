package org.ryjan.telegram.services;

import org.ryjan.telegram.dao.UserDAOImpl;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;
import org.ryjan.telegram.utils.HibernateSessionFactory;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    public UserDatabase findUser(long id) {
        return userDAO.findUser(id);
    }

    public UserDatabase findUser(String username) {
        return userDAO.findUser(username);
    }

    public BankDatabase findBank(long id) {
        return userDAO.findBank(id);
    }

    public boolean userIsExist(long id) {
        return findUser(id) != null;
    }

    public void save(UserDatabase userDatabase) {
        userDAO.save(userDatabase);
    }

    public void update(UserDatabase userDatabase) {
        userDAO.update(userDatabase);
    }

    public void delete(UserDatabase userDatabase) {
        userDAO.delete(userDatabase);
    }
}
