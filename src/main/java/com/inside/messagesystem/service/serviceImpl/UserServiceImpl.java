package com.inside.messagesystem.service.serviceImpl;

import com.inside.messagesystem.dto.request.RegistrationRequestDto;
import com.inside.messagesystem.entity.RoleEntity;
import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.exception.BusinessException;
import com.inside.messagesystem.exception.SecurityException;
import com.inside.messagesystem.repository.UserRepository;
import com.inside.messagesystem.security.JwtTokenUtil;
import com.inside.messagesystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void registration(RegistrationRequestDto dto) throws SQLException {
        UserEntity user = new UserEntity();
        RoleEntity role = new RoleEntity();
        role.setId(1);
        user.setName(dto.getName());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(12)));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    @Override
    public UserEntity getUserFromToken(String token) {
        return userRepository.findByName(jwtTokenUtil.getUserNameFromToken(token))
                .orElseThrow(() -> new SecurityException(HttpStatus.NOT_FOUND, "User with such credentials does not exist"));
    }

    @Override
    public UserEntity getUserByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
                String.format("User with name %s does not registered", name)));
    }
}
