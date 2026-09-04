package com.sweng861.agiletracker.service;

import com.sweng861.agiletracker.model.User;
import com.sweng861.agiletracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserSyncService {

    private static final Logger logger = LoggerFactory.getLogger(UserSyncService.class);

    private final UserRepository userRepository;

    public UserSyncService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void upsertFromOAuth(String providerId, String email) {
        if (providerId == null || email == null) {
            logger.warn("OAuth user missing providerId or email: providerId={}, email={}", providerId, email);
            return;
        }

        Optional<User> existingUser = userRepository.findByProviderId(providerId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(email);
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            User saved = userRepository.save(user);
            logger.info("Updated existing user id={} providerId={} email={}",
                    saved.getId(), saved.getProviderId(), saved.getEmail());
        } else {
            User saved = userRepository.save(new User(providerId, email));
            logger.info("Created new user id={} providerId={} email={}",
                    saved.getId(), saved.getProviderId(), saved.getEmail());
        }
    }
}
