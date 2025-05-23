package com.scm.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.Exception.Helper;
import com.scm.entities.User;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    //User Dashboard
    @RequestMapping(value = "/dashboard")
    public String userDashboard()
    {
        return "user/dashboard";
    }
    //User Profile
    @RequestMapping(value = "/profile")
    public String userProfile(Model model,Authentication authentication)
    {
        String username = Helper.getEmailOfLoggedInUser(authentication);

       User  user = userService.getUserByEmail(username);
        logger.info("User Logged in {}",user.getEmail());
        logger.info("Name is {}", user.getName());

        model.addAttribute("LoggedInUser", user);
        return "user/profile";
    }



}
