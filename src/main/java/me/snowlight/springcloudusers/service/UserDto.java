package me.snowlight.springcloudusers.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.snowlight.springcloudusers.controller.ResponseeOrder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private LocalDateTime createdAt;

    private String encryptedPwd;

    private List<ResponseeOrder> orders;
}
