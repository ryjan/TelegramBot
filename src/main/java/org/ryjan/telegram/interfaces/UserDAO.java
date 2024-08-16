package org.ryjan.telegram.interfaces;

import org.ryjan.telegram.database.Bank;
import org.ryjan.telegram.database.User;

public interface UserDAO {
    public User findById(int id);
    public Bank findBankById(int id);
    public void save(User user);
    public void update(User user);
    public void delete(User user);
}
