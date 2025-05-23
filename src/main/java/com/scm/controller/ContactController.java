package com.scm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.Exception.AppConstant;
import com.scm.Exception.Helper;
import com.scm.Exception.message;
import com.scm.Exception.messageType;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    @Autowired
    private ImageService imageService;

   private Logger logger=LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private  ContactService contactService;
    @Autowired
    private UserService userService;
    @RequestMapping("/add")
    public String addContactView(Model model)
    {       
        ContactForm contactForm = new ContactForm();
        contactForm.setName("Arup Kumar Singh");
        contactForm.setFavourite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }
   

   @RequestMapping(value = "/add", method=RequestMethod.POST)   
    public String saveContact(@Valid  @ModelAttribute ContactForm contactForm ,BindingResult result,Authentication authentication,
    HttpSession session)
    {             
        String username = Helper.getEmailOfLoggedInUser(authentication);        
            User user = userService.getUserByEmail(username);
                if(result.hasErrors())
                {
                    result.getAllErrors().forEach(error -> logger.info(error.toString()));
                     session.setAttribute("message", message.builder()
                    .content("Please correct the following errors")
                    .type(messageType.red)
                    .build());
                    return "user/add_contact";
                }

                String filename = UUID.randomUUID().toString();
                String fileUrl=imageService.uploadImage(contactForm.getContactimage(),filename);

                Contact contact = new Contact();
                contact.setName(contactForm.getName());
                contact.setEmail(contactForm.getEmail());
                contact.setAddress(contactForm.getAddress());
                contact.setFavourite(contactForm.isFavourite());
                contact.setDescription(contactForm.getDescription());
                contact.setPhonenumber(contactForm.getPhonenumber());
                contact.setLinkedLink(contactForm.getLinkedLink());
                contact.setWebsiteLink(contactForm.getWebsiteLink());
                contact.setPicture(fileUrl);
                contact.setCloudinaryImagePublicId(filename);
                contact.setUser(user);
                contactService.save(contact);
                System.out.println(contactForm);
                session.setAttribute("message",
                message.builder()
                        .content("You have successfully added a new contact")
                        .type(messageType.green)
                        .build());

        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContact(
        @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue =AppConstant.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication){

       String username= Helper.getEmailOfLoggedInUser(authentication);
       User user = userService.getUserByEmail(username);
       Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

       model.addAttribute("pageContact", pageContact);
       model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        return"user/contacts";
    }


    //Search handler 
    @RequestMapping("/search")
    public String searchHandler(
        @RequestParam("field") String field,
        @RequestParam("keyword") String value,
       @ RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue =AppConstant.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication
    )
    {
        logger.info("field {}, keyword {}",field,value);
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact=null;
        if(field.equalsIgnoreCase("name"))
        {
            pageContact=contactService.searchByName(value, size, page, sortBy, direction,user);
        }
        else if (field.equalsIgnoreCase("email")) 
        {
            pageContact=contactService.searchByEmail(value, size, page, sortBy, direction,user);
        }
        else if (field.equalsIgnoreCase("phone")) 
        {
            pageContact=contactService.searchByphonenumber(value, size, page, sortBy, direction,user);
        }
        logger.info("pageContact {}",pageContact);

        model.addAttribute("pageContact", pageContact);
        return "user/search";
    }

}
