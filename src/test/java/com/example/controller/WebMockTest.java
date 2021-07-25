package com.example.controller;

import static com.example.builder.UserBuilder.userBuilder;
import static com.example.mapper.UserMapper.objectToJson;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.example.exception.ResourceNotFoundException;
import com.example.model.UserEntity;
import com.example.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class)
public class WebMockTest {

    private static final int ID = 1;

    private static final String PATH_ID_1 = ("/api/v1/users/" + ID);

    private static final String PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;

    @Test
    public void whenGetUsersThenReturnOk() throws Exception{

        UserEntity user = userBuilder();

        List<UserEntity> userList = List.of(user);

        String userJsonList = objectToJson(userList);

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(userJsonList));
    }

    @Test
    public void whenGetUserByIdThenReturnOk() throws Exception{

        UserEntity user = userBuilder();

        String userJson = objectToJson(user);

        when(userService.getById(ID)).thenReturn(user);

        mockMvc.perform(get(PATH_ID_1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json(userJson));
    }

    @Test
    public void whenGetUserThatDoesntExistThrowsExceptionBadRequest() throws Exception{

        when(userService.getById(ID)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(PATH_ID_1))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostUserThenReturnCreated() throws Exception{

        UserEntity user = userBuilder();

        String userJson = objectToJson(user);

        Mockito.any();

        when(userService.postUser(user)).thenReturn(user);

        mockMvc.perform(post(PATH)
                .contentType(APPLICATION_JSON)
                .content(userJson)
                .accept(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json(userJson));
    }

    @Test
    public void whenDeleteUserThenReturnNoContent() throws Exception {

        Mockito.doNothing().when(userService).deleteUser(ID);

        mockMvc.perform(delete(PATH_ID_1))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteUserDoesntExistThenReturnNoContent() throws Exception {

        Mockito.doThrow(ResourceNotFoundException.class).when(userService).deleteUser(ID);

        mockMvc.perform(delete(PATH_ID_1))
            .andExpect(status().isBadRequest());
    }
}
