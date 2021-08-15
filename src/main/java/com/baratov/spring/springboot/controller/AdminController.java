package com.baratov.spring.springboot.controller;

import com.baratov.spring.springboot.model.Role;
import com.baratov.spring.springboot.model.User;
import com.baratov.spring.springboot.myExcetion.SaveObjectException;
import com.baratov.spring.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/")
public class AdminController {

    private IUserService userService;

    @Autowired
    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("newUser")
    public User newPerson() {
        return new User();
    }
//
//    @GetMapping("/admin")
//    public ModelAndView userProfileUpdate(Principal principal) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("userName", userEntity.getName());
//        modelAndView.addObject("userSurname", userEntity.getSurname());
//        modelAndView.addObject("userMiddleName", userEntity.getMiddleName());
//        modelAndView.addObject("userPhone", userEntity.getPhone());
//        return modelAndView;
//    }

    @GetMapping("/admin")
    public String index(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("currentUserName", currentUser.getUsername());

        model.addAttribute("currentUserRoles", currentUser.getRoles()
                .toString()
                .replace("[", "")
                .replace("ROLE_","")
                .replace("]",""));
        model.addAttribute("currentUserName", principal.getName());
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("people", userService.getAllUsers());
        return "view/index";
    }

    @PostMapping("/admin")
    public String creat(@ModelAttribute("newUser") @Valid User user,
                        BindingResult bindingResult, Model model,
                        @RequestParam(name = "listRoles[]", required = false) String... roles) {
        model.addAttribute("people", userService.getAllUsers());
        if (bindingResult.hasErrors()) {
            return "view/index";
        }
        try {
            Set<Role> roleSet = userService.getSetRoles(roles);
            user.setRoles(roleSet);
            userService.registrationUser(user);
        } catch (SaveObjectException e) {
            e.getMessage();
            bindingResult.rejectValue("username", "SaveObjectException",
                    "Exception: The user with the name " + user.getUsername() + " already exists");
            return "view/index";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deletePerson(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(@ModelAttribute("id") Long id, Model model) {
        model.addAttribute("updatePerson", userService.getUserById(id));
        return "view/edit";
    }

    @PatchMapping("/admin/{id}")
    public String updatePerson(@ModelAttribute("updatePerson") @Valid User updateuser,
                               BindingResult bindingResult,
                               @RequestParam(name = "listRoles[]", required = false) String... roles) {
        if (bindingResult.hasErrors()) {
            return "view/edit";
        }
        try {
            Set<Role> roleSet = userService.getSetRoles(roles);
            updateuser.setRoles(roleSet);
            userService.updateUser(updateuser);
        } catch (SaveObjectException e) {
            e.getMessage();
            bindingResult.rejectValue("username", "SaveObjectException",
                    "Exception: The user with the name " + updateuser.getUsername() + " already exists");
            return "view/edit";
        }
        return "redirect:/admin";
    }
    ///////////////
//    @GetMapping("/admin/{id}/edit") //ModelAndView
//    public ModelAndView editt(@ModelAttribute("id") Long id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return new ModelAndView("edit","user",userService.getUserById(id));
//    }
}