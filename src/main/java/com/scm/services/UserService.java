package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.User;

public interface UserService {

        User SaveUser(User user);

        Optional<User> getUserById(String Id);

        Optional<User> UpdateUser(User user);

        void DeleteUser(String Id);

        boolean isUserExist(String UserId);

        boolean isUserExistByEmail(String email);

        List<User> getAllUsers();

        User getUserByEmail(String email);
}
