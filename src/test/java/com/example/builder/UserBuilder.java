package com.example.builder;

import com.example.model.UserEntity;

public class UserBuilder {
    
    public static UserEntity userBuilder() {
        return UserEntity.builder()
            .id(1)
            .build();
    }
}
