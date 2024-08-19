package org.ryjan.telegram.interfaces;

import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;

public interface UserDAO {
    public UserDatabase findById(long id);
    public BankDatabase findBankById(long id);
    public void save(UserDatabase userDatabase);
    public void update(UserDatabase userDatabase);
    public void delete(UserDatabase userDatabase);
}
