package me.snowlight.springcloudusers.service;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private LocalDateTime createdAt;

    private String encryptedPwd;
}
