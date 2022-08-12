package me.snowlight.springcloudusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.springcloudusers.controller.ResponseOrder;
import me.snowlight.springcloudusers.model.UserEntity;
import me.snowlight.springcloudusers.model.UserRepository;
import me.snowlight.springcloudusers.model.repository.OrderServiceClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OrderServiceClient orderServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userRepository.save(userEntity);
        return null;
    }

    @Override
    public Iterable<UserDto> getUserByAll() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        
        List<UserDto> usersDto = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            usersDto.add(new ModelMapper().map(userEntity, UserDto.class));
        }

        return usersDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(IllegalArgumentException::new);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        log.info("Before call orders microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(userId), 
            throwable -> new ArrayList<>());
        log.info("after call orders microservice");

        userDto.setOrders(orders);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).
                                        orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), 
                            true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserByEmail(String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow();
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }
    
}
