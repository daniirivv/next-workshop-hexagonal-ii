package com.music.streaming.user.application.port;

import com.music.streaming.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    List<User> getUsers();
    Optional<User> getUserById(String id);
    Optional<User> getUserByEmail(String email);
    void deleteUser(String id);
    void updateUser(User user);
    void createUser(User user);
}
