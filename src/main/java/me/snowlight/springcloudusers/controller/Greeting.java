package me.snowlight.springcloudusers.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Greeting {
    @Value("${greeting.message}")
    public String message;

    public String getMessage() {
        return message;
    }
}
