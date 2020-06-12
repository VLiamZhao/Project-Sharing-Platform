package com.psp.service;

import com.psp.model.User;
import com.psp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    UserDao userDao;

    public List<User> getStudents(){
        return userDao.getStudents();
    }
}
