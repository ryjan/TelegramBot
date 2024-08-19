package org.ryjan.telegram.services;

import org.ryjan.telegram.dao.UserDAOImpl;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;

public class UserService implements UserDAO {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public UserDatabase findById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public BankDatabase findBankById(long id) {
        return userDAO.findBankById(id);
    }

    @Override
    public void save(UserDatabase userDatabase) {
        userDAO.save(userDatabase);
    }

    @Override
    public void update(UserDatabase userDatabase) {
        userDAO.update(userDatabase);
    }

    @Override
    public void delete(UserDatabase userDatabase) {
        userDAO.delete(userDatabase);
    }
}
