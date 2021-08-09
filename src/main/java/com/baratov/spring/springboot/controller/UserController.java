package com.baratov.spring.springboot.controller;

import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {
    private IUserService dao;

    @Autowired
    public UserController(IUserService dao) {
        this.dao = dao;
    }

    @ModelAttribute("newUser")
    public User getPerson() {
        return new User();
    }

    @GetMapping("/user")
    public String index(Model model, Principal principal) {
        model.addAttribute("people", dao.findByUsername(principal.getName()));
        return "view/index";
    }
}
