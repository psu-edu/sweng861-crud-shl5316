package com.sweng861.agiletracker.service;

import com.sweng861.agiletracker.model.User;
import com.sweng861.agiletracker.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);

        String providerId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");

        if (providerId == null || email == null) {
            return oauthUser;
        }

        Optional<User> existingUser = userRepository.findByProviderId(providerId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(email);
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        } else {
            User user = new User(providerId, email);
            userRepository.save(user);
        }

        return oauthUser;
    }
}
