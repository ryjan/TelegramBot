package org.ryjan.telegram.services;

import org.ryjan.telegram.dao.UserDAOImpl;
import org.ryjan.telegram.database.Bank;
import org.ryjan.telegram.database.User;
import org.ryjan.telegram.interfaces.UserDAO;

public class UserService implements UserDAO {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public Bank findBankById(int id) {
        return userDAO.findBankById(id);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }
}
