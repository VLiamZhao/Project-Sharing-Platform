package com.psp.service;

import com.psp.model.User;
import com.psp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getUserByCredentials(String email, String password){
        return userDao.getUserByCredentials(email, password);
    }

    public User save(User user){
        return userDao.save(user);
    }

    public  User getUserById(long id){
        return userDao.getUserById(id);
    }

    public boolean deleteUserById(long id){
        return userDao.deleteUserById(id);
    }

    public List<User> gerAllUsers(){
        return userDao.getUsers();
    }

    public User updateUser(User user){
        return userDao.updateUser(user);
    }

    public User getUserByEmail(String email){
        return userDao.getUserByEmail(email);
    }
}
