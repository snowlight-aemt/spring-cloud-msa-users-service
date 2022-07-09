package me.snowlight.springcloudusers.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.springcloudusers.service.UserDto;
import me.snowlight.springcloudusers.service.UserService;

@Slf4j
@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UsersController {
    private final Greeting greeting;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getAllUsers() {
        List<ResponseUser> responseUsers = new ArrayList<>();

        for (UserDto userDto : userService.getUserByAll()) {
            responseUsers.add(new ModelMapper().map(userDto, ResponseUser.class));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseUsers);
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("user_id") String userId) {
        ResponseUser responseUser = new ModelMapper().map(userService.getUserByUserId(userId), ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        log.info(user.toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("it's Working in User SErvice on port %s", request.getServerPort());
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }
}
