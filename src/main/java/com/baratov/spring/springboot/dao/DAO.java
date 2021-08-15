package com.baratov.spring.springboot.dao;

import com.baratov.spring.springboot.model.Role;
import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.myExcetion.SaveObjectException;

import java.util.List;
import java.util.Set;

public interface DAO {
    void saveUser(User user) throws SaveObjectException;

    void updateUser(User updateUser) throws SaveObjectException;

    void removeUserById(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User findByUserEmail(String username);

    Set<Role> getSetRoles(String... roles);
}
