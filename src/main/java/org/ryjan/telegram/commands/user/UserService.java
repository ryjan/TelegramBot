package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.dao.UserDAOImpl;
import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.interfaces.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { // сделать Jpa repository к сервису и все будет работать
    private final UserDatabaseRepository userDatabaseRepository;

    @Autowired
    public UserService(UserDatabaseRepository userDatabaseRepository) {
        this.userDatabaseRepository = userDatabaseRepository;
    }

    public UserDatabase findUser(long id) {
        return userDatabaseRepository.findUserDatabaseById(id);
    }

    public UserDatabase findUser(String username) {
        return userDatabaseRepository.findUserDatabaseByUserTag(username);
    }

    public BankDatabase findBank(long id) {
        return userDatabaseRepository.findBankDatabaseById(id);
    }

    public boolean userIsExist(long id) {
        return userDatabaseRepository.existsUserDatabaseById(id);
    }

    public boolean userIsExist(String username) {
        return userDatabaseRepository.existsUserDatabaseByUserTag(username);
    }

    public void update(UserDatabase userDatabase) {
        userDatabaseRepository.save(userDatabase);
    }

    public void delete(UserDatabase userDatabase) {
        userDatabaseRepository.delete(userDatabase);
    }
}
