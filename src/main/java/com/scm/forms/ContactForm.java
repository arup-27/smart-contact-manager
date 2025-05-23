package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$",message = "Invalid phone number")
    private String phonenumber;

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;
    private String Description;
    private boolean favourite;
    private String WebsiteLink;
    private String LinkedLink;

    private MultipartFile contactimage;

}
