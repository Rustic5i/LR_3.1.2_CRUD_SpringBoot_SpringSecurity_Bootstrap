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
    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("newUser")
    public User getPerson() {
        return new User();
    }

    @GetMapping("/user")
    public String index(Model model, Principal principal) {
        User currentUser = userService.findByUserEmail(principal.getName());

        model.addAttribute("currentUserEmail", currentUser.getEmail());

        model.addAttribute("currentUserRoles", currentUser.getRoles()
                .toString()
                .replace("[", "")
                .replace("ROLE_", "")
                .replace("]", ""));

        model.addAttribute("currentUser", currentUser);
        return "view/user";
    }
}
