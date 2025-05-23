package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.Exception.AppConstant;
import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.repositeries.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    @Autowired
    private UserRepo userRepo;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                logger.info("OAuthenticationSuccessHandler");

                   var oauth2AuthenicationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenicationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstant.ROLE_USER));
        user.setEmailverified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // google
            // google attributes

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilepic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setPoviderUserID(oauthUser.getName());
            user.setProvide(Providers.GOOGLE);
            user.setAbout("This account is created using google.");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // github
            // github attributes
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilepic(picture);
            user.setName(name);
            user.setPoviderUserID(providerUserId);
            user.setProvide(Providers.GITHUB);

            user.setAbout("This account is created using github");
        }

        else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

        }

        else {
            logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
        }


                // DefaultOAuth2User user= (DefaultOAuth2User) authentication.getPrincipal();
                // // logger.info(user.getName());
                // // user.getAttributes().forEach((key,value)->{
                // //     logger.info("{} => {}",key ,user );
                // // });
                // // logger.info(user.getAuthorities().toString());

                // String name = user.getAttribute("name").toString();
                // String email = user.getAttribute("email").toString();
                // String picture = user.getAttribute("picture").toString();

                // User user1 = new User();

                // user1.setEmail(email);
                // user1.setUsername(name);
                // user1.setProfilepic(picture);
                // user1.setPassword("password");
                // user1.setUserId(UUID.randomUUID().toString());
                // user1.setProvide(Providers.GOOGLE);
                // user1.setEnabled(true);

                // user1.setEmailverified(true);
                // user1.setPoviderUserID(user.getName());
                // user1.setRoleList(List.of(AppConstant.ROLE_USER));
                // user1.setAbout("Created by google");

                User user2 =userRepo.findByEmail(user.getEmail()).orElse(null);
                if (user2 ==null) {
                    userRepo.save(user);
                    logger.info("User saved " + user.getEmail());
                }
                new DefaultRedirectStrategy().sendRedirect(request, response,"/user/profile");
    }

}
