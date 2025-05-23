package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.Exception.AppConstant;
import com.scm.Exception.ResourceNotFoundException;
import com.scm.entities.User;
import com.scm.repositeries.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User SaveUser(User user) {
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstant.ROLE_USER));
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String Id) {
        return userRepo.findById(Id);
    }

    @Override
    public Optional<User> UpdateUser(User user) {

        User user2 = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        user2.setName(user.getUsername());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setPhonenumber(user.getPhonenumber());
        user2.setAbout(user.getAbout());
        user2.setProfilepic(user.getProfilepic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailverified(user.isEmailverified());
        user2.setPhoneverified(user.isPhoneverified());
        user2.setProvide(user.getProvide());
        user2.setPoviderUserID(user.getPoviderUserID());

        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void DeleteUser(String Id) {
        User user2 = userRepo.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        userRepo.delete(user2);

    }

    @Override
    public boolean isUserExist(String UserId) {
        User user2 = userRepo.findById(UserId).orElseThrow(null);
        return user2 != null ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElseThrow(null);
        return user2 != null ? true : false;

    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

}
