package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.User;

import java.util.List;

public interface UserService {

    User save(User theUser);

    List<User> findUser();

    User findById(long theId);

    void deleteById(long theId);

    User findByEmail(String email);

    void updatePassword(User user, String newPassword);
}