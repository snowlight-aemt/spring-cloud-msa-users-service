package me.snowlight.springcloudusers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import me.snowlight.springcloudusers.model.UserEntity;

public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto userDto);
    
    public Iterable<UserDto> getUserByAll();

    public UserDto getUserByUserId(String userId);

    public UserDto getUserByEmail(String username);
    
}
