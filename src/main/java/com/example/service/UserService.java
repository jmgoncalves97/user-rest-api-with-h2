package com.example.service;

import java.util.List;

import com.example.exception.ResourceNotFoundException;
import com.example.model.UserEntity;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    public UserEntity postUser(UserEntity user) {
        var userSaved = userRepository.save(user);
        return userSaved;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        return allUsers;
    }

    public UserEntity getById(int id) throws ResourceNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Usuario com id %d não encontrado", id)));
        return user;
    }

    public UserEntity putUser(UserEntity u, int id) throws ResourceNotFoundException {
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Usuario com id %d não encontrado.", id)));

        UserEntity user = userRepository.findById(id).get();

        user.setName(u.getName());
        user.setRole(u.getRole());

        return userRepository.save(user);
    }

    public void deleteUser(int id) throws ResourceNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Usuario com id %d não encontrado.", id)));

        userRepository.delete(user);
    }
}
