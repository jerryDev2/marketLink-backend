package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.User;
import com.BeninCodeX.MarketLink.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User theUser) {

        String hashPassword =
                passwordEncoder.encode(theUser.getPassword());

        theUser.setPassword(hashPassword);

        return userRepository.save(theUser);
    }

    @Override
    public List<User> findUser() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long theId) {

        Optional<User> result =
                userRepository.findById(theId);

        if (result.isPresent()) {
            return result.get();
        }

        throw new RuntimeException(
                "The user id was not found " + theId
        );
    }

    @Override
    public void deleteById(long theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void updatePassword(
            User user,
            String newPassword
    ) {

        String hashedPassword =
                passwordEncoder.encode(newPassword);

        user.setPassword(hashedPassword);

        userRepository.save(user);
    }
}