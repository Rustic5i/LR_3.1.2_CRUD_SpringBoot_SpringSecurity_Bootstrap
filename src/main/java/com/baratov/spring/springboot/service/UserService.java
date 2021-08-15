package com.baratov.spring.springboot.service;

import com.baratov.spring.springboot.dao.DAO;
import com.baratov.spring.springboot.model.Role;
import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.myExcetion.SaveObjectException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService, IUserService {

    private DAO dao;

    private PasswordEncoder encoder;

    @Autowired
    private HttpServletRequest req;
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    public UserService(DAO dao, PasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found ", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.getAuthorities());
    }

    @Override
    @Transactional
    public User findByUserEmail(String userEmail) {
        User user = dao.findByUserEmail(userEmail);
        Hibernate.initialize(user.getAuthorities());
        return user;
    }

    //мы говорим Spring, эй, spring,
    // если ты видишь какое-либо исключение, Runtime exception или Checked exception,
    // пожалуйста, откатите транзакцию (не сохраняйте запись в БД) Аминь !
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void registrationUser(User newUser) throws SaveObjectException {
        User user = new User();
        user.setLastName(newUser.getLastName());
        user.setFirstName(newUser.getFirstName());
        user.setAge(newUser.getAge());
        user.setEmail(newUser.getEmail());
        user.setRoles(newUser.getRoles());
        user.setPassword(encoder.encode(newUser.getPassword()));
        dao.saveUser(user);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> users = dao.getAllUsers();
        if (users.size() == 0) {
            return null;
        }
        Hibernate.initialize(users.get(0).getAuthorities());
        return users;
    }

    @Override
    @Transactional
    public Set<Role> getSetRoles(String... roles) {
        return dao.getSetRoles(roles);
    }

    @Override
    @Transactional
    public void removeUserById(Long id) {
        dao.removeUserById(id);
    }

    @Override
    public User getUserById(Long id) {
        return dao.getUserById(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateUser(User updateUser) throws SaveObjectException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = dao.findByUserEmail(authentication.getName());
        if ((currentUser.getId() == updateUser.getId()) && !(currentUser.getEmail().equals(updateUser.getUsername()))) {
            dao.updateUser(updateUser);
            updatePrincipal(updateUser);
        } else {
            dao.updateUser(updateUser);
        }
    }

    public void updatePrincipal(User updateUser) {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(updateUser, updateUser.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //////////////
//        UsernamePasswordAuthenticationToken authReq
//                = new UsernamePasswordAuthenticationToken(updateUser.getUsername(), "111");
//        Authentication auth = authManager.authenticate(authReq); //сверяем данные с бд
//
//        SecurityContext sc = SecurityContextHolder.getContext(); //получаем хранилище
//        sc.setAuthentication(auth);                          // ложем  в хранилище объектов
//        HttpSession session = req.getSession(true);
//        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }
}
