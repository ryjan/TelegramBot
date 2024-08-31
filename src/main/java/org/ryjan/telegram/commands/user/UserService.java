package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.model.BankDatabase;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.repos.BankDatabaseRepository;
import org.ryjan.telegram.repos.UserDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService { // сделать Jpa repository к сервису и все будет работать
    @Autowired
    private UserDatabaseRepository userDatabaseRepository;
    @Autowired
    private BankDatabaseRepository bankDatabaseRepository;

    public UserDatabase findUser(String username) {
        return userDatabaseRepository.findUserDatabaseByUserTag(username);
    }

    public UserDatabase findUser(Long id) {
        return userDatabaseRepository.findById(id).orElse(null);
    }

    public BankDatabase findBank(long id) {
        return null;
    }

    public boolean userIsExist(long id) {
        return userDatabaseRepository.existsById(id);
    }

    public boolean userIsExist(String username) {
        return userDatabaseRepository.existsByUserTag(username);
    }

    public void update(UserDatabase userDatabase) {
        userDatabaseRepository.save(userDatabase);
    }

    public void delete(UserDatabase userDatabase) {
        userDatabaseRepository.delete(userDatabase);
    }

    @Transactional
    public UserDatabase createUser(long id, String username) {
        UserDatabase userDatabase = new UserDatabase(id, username);
        return userDatabaseRepository.save(userDatabase);
    }
}
