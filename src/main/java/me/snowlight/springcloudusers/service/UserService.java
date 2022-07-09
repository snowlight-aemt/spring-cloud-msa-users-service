package me.snowlight.springcloudusers.service;

import java.util.List;
import java.util.Optional;

import me.snowlight.springcloudusers.model.UserEntity;

public interface UserService {

    public UserDto createUser(UserDto userDto);
    
    public Iterable<UserDto> getUserByAll();

    public UserDto getUserByUserId(String userId);
    
}
