package com.psp.repository;

import com.psp.model.User;
import java.util.List;
import java.util.Set;

public interface UserDao {
    User save(User user);
    boolean deleteUserById(long id);
    List<User> getUsers();
    User updateUser(User user);
    User getUserById(long id);
    User getUserByCredentials(String email, String password);
    List<User> getStudents();
    User getUserByEmail(String email);
}
