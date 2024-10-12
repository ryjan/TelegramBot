package org.ryjan.telegram.services;

import org.ryjan.telegram.model.users.BankDatabase;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.repos.jpa.BankDatabaseRepository;
import org.ryjan.telegram.repos.jpa.JpaUserDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private JpaUserDatabaseRepository userDatabaseRepository;

    @Autowired
    private BankDatabaseRepository bankDatabaseRepository;

    public UserDatabase findUser(String username) {
        return userDatabaseRepository.findByUserTag(username);
    }

    public UserDatabase findUser(Long id) {
        return userDatabaseRepository.findById(id).orElse(null);
    }

    public BankDatabase findBank(Long id) {
        return null;
    }

    public boolean userIsExist(Long id) {
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
