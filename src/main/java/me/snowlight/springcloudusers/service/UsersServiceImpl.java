package me.snowlight.springcloudusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import me.snowlight.springcloudusers.controller.ResponseOrder;
import me.snowlight.springcloudusers.model.UserEntity;
import me.snowlight.springcloudusers.model.UserRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RestTemplate restTemplate;

    private final Environment env;

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

        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> response = restTemplate.exchange(orderUrl,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<ResponseOrder>>() {});

        userDto.setOrders(response.getBody());
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
