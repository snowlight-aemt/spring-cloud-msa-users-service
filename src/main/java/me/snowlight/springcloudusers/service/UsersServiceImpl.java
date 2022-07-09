package me.snowlight.springcloudusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bouncycastle.jcajce.provider.keystore.BC;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.snowlight.springcloudusers.controller.ResponseeOrder;
import me.snowlight.springcloudusers.model.UserEntity;
import me.snowlight.springcloudusers.model.UserRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

        List<ResponseeOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return userDto;
    }
    
}
