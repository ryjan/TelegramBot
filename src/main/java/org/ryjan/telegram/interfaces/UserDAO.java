package org.ryjan.telegram.interfaces;

import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;

public interface UserDAO {
    public UserDatabase findUser(long id);
    public UserDatabase findUser(String username);
    public BankDatabase findBank(long id);
    public BankDatabase findBank(String username);
    public boolean isOwner(long id);
    public boolean isOwner(String username);
    public void save(UserDatabase userDatabase);
    public void update(UserDatabase userDatabase);
    public void delete(UserDatabase userDatabase);
    public default Boolean userIsExist(long id) {
        return findUser(id) != null;
    }
}
