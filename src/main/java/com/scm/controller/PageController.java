package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.Exception.message;
import com.scm.Exception.messageType;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
   public String index()
   {
    return "redirect:/home";
   }
    @RequestMapping("/home")
   public String HomPage(Model model){
    System.out.println("Home Page Handler");
    model.addAttribute("name", "Spring technologies");
    model.addAttribute("youtubeChannel", "learn code with Durgesh");
    model.addAttribute("githubRepo", "https://github.com/learncodewithdurgesh/scm-springboot");
    return "Home";
   }
   

   //About page
   @RequestMapping("/about")
   public String AboutPage()
   {
    System.out.println("About page loading");
    return "About";
   }

   //Services
   @RequestMapping("/service")
   public String ServicesPage()
   {
    System.out.println("Services page loading");
    return "Service";
   }
   @GetMapping("/register")
   public String registerPage(Model model) {

        UserForm userForm= new UserForm();
        model.addAttribute("userForm", userForm);
       return "register";
   }
   @GetMapping("/login")
   public String LoginPage() {
       return new String("login");
   }
   @GetMapping("/contact")
   public String contactPage() {
       return new String("contact");
   }
   
   //process Register

   @RequestMapping(value = "/do-register",method = RequestMethod.POST)
   public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult ,HttpSession session)
   {
    System.out.println(userForm);
    System.out.println("Process Register");
    // User user =User.builder()
    //   .username(userForm.getName())
    //   .email(userForm.getEmail())
    //   .password(userForm.getPassword())
    //   .about(userForm.getAbout())
    //   .phonenumber(userForm.getPhonenumber())
    // .build();
    if(rBindingResult.hasErrors())
    {
        return "register";
    }

    User user = new User();
    user.setName(userForm.getName());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());
    user.setAbout(userForm.getAbout());
    user.setPhonenumber(userForm.getPhonenumber());

    User saveUser=userService.SaveUser(user);

    System.out.println("saved User");

    //message passing
    message Message=message.builder().content("Registration Successful").type(messageType.green).build();
   session.setAttribute("message",Message);

    return "redirect:/register";
   }
   
}
