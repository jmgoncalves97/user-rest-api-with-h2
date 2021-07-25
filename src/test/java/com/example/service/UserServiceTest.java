package com.example.service;

import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.builder.UserBuilder;
import com.example.exception.ResourceNotFoundException;
import com.example.model.UserEntity;
import com.example.repository.UserRepository;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    
    private static final Integer ID = 1;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenPutUserThenReturnUser() throws ResourceNotFoundException {

        UserEntity user = UserBuilder.userBuilder();

        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        MatcherAssert.assertThat(userService.putUser(user, ID), Matchers.is(Matchers.equalTo(user)));
    }

    @Test
    public void whenPutUserThatIdDoesntExistThenThrowsException() {

        UserEntity user = UserBuilder.userBuilder();

        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.putUser(user, ID));
    }
}
