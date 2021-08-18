package com.baratov.spring.springboot.service;

import com.baratov.spring.springboot.model.Role;
import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.myExcetion.SaveObjectException;
import java.util.List;
import java.util.Set;

public interface IUserService {
    User findByUserEmail(String userEmail);

    void registrationUser(User newUser) throws SaveObjectException;

    List<User> getAllUsers();

    Set<Role> getSetRoles(String... roles);

    void removeUserById(Long id);

    User getUserById(Long id);

    void updateUser(User updateUser) throws SaveObjectException;
}
