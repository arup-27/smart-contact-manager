package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    
    @NotBlank(message = "Username is required.")
    @Size(min = 3,message = "Minimum 3 character required.")
    private String name;
    @NotBlank(message = "Password is required")
    @Size(min = 6,message = "Minimum 6 character required.")
    private String password;
    @Email(message = "Email not valid.")
    @NotBlank(message = "Email is required.")
    private String email;
   @NotBlank(message = "About is required.")
    private String about;
    @Size(min = 8,max = 12,message = "Invalid Phone Number.")
    private String phonenumber;

}
