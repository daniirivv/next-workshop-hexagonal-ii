package com.music.streaming.user.infrastructure.repository;

import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.infrastructure.repository.entity.UserEntity;
import com.music.streaming.user.infrastructure.repository.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    final UserEntityMapper userEntityMapper;
    final UserJpaRepository userJpaRepository;

    @Override
    public List<User> getUsers() {
        return userJpaRepository.findAll().stream().map(userEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userJpaRepository.findById(id).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        List<UserEntity> users = userJpaRepository.findByEmail(email);
        return users.isEmpty() ? Optional.empty() : users.stream().findFirst().map(userEntityMapper::toDomain);
    }

    @Override
    public void deleteUser(String id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userJpaRepository.save(userEntityMapper.fromDomain(user));
    }

    @Override
    public void createUser(User user) {
        userJpaRepository.save(userEntityMapper.fromDomain(user));
    }
}
