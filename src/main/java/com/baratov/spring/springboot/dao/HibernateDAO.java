package com.baratov.spring.springboot.dao;

import com.baratov.spring.springboot.model.Role;
import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.myExcetion.SaveObjectException;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class HibernateDAO implements DAO {

    @PersistenceContext()
    private EntityManager entityManager;


    @Override
    public void saveUser(User user) throws SaveObjectException {
        try {
            entityManager.persist(user);
        } catch (PersistenceException e) {
            throw new SaveObjectException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User updateUser) throws SaveObjectException {
        User user = findByUserEmail(updateUser.getEmail());

        if (user != null && user.getId() != updateUser.getId()) {
            throw new SaveObjectException("Exception: User с таким Email уже существует");
        }
        entityManager.merge(updateUser);
    }

    @Override
    public void removeUserById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
        return users;
    }

    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUserEmail(String userEmail) {
        User user = new User();
        try {
            user = entityManager.createQuery("SELECT user FROM User user WHERE user.email = :username", User.class)
                    .setParameter("username", userEmail)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    @Override
    public Set<Role> getSetRoles(String... roles) { // Использование List  в параметрах HQL запроса
        Set<Role> roleSet;
        roleSet = entityManager
                .createQuery("SELECT role FROM Role role WHERE role.authority IN (:roles)"
                        , Role.class)
                .setParameter("roles", Arrays.asList(roles))
                .getResultStream().collect(Collectors.toSet());
        return roleSet;
    }
}
