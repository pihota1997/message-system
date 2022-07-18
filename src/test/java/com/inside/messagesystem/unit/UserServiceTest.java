package com.inside.messagesystem.unit;

import com.inside.messagesystem.dto.request.RegistrationRequestDto;
import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.exception.BusinessException;
import com.inside.messagesystem.exception.SecurityException;
import com.inside.messagesystem.repository.UserRepository;
import com.inside.messagesystem.security.JwtTokenUtil;
import com.inside.messagesystem.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Captor
    private ArgumentCaptor<UserEntity> argumentCaptor;

    @InjectMocks
    private UserServiceImpl underTest;

    private UserEntity user;

    @BeforeEach
    void init() {
        user = new UserEntity();
        user.setName("user");
        user.setPassword("password");
    }

    @Test
    void shouldSuccessRegisterUser() throws SQLException {
        //given
        RegistrationRequestDto dto = new RegistrationRequestDto(user.getName(), user.getPassword());

        //when
        underTest.registration(dto);

        //then
        verify(userRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getName(), user.getName());
        assertTrue(BCrypt.checkpw(user.getPassword(), argumentCaptor.getValue().getPassword()));
        assertNotNull(argumentCaptor.getValue().getRoles());
    }

    @Test
    void shouldSuccessGetUserFromToken() {
        //given
        String token = "token";
        when(userRepository.findByName(any())).thenReturn(Optional.of(user));

        //when
        UserEntity userFromToken = underTest.getUserFromToken(token);

        //then
        assertEquals(user, userFromToken);
        verify(jwtTokenUtil, times(1)).getUserNameFromToken(token);
        verify(userRepository, times(1)).findByName(any());
    }

    @Test
    void shouldFailGetUserFromTokenIfUserIsNotRegistered() {
        //given
        String token = "token";

        //expected
        SecurityException ex = assertThrows(SecurityException.class, () -> underTest.getUserFromToken(token));

        //then
        assertEquals("User with such credentials does not exist", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(jwtTokenUtil, times(1)).getUserNameFromToken(token);
        verify(userRepository, times(1)).findByName(any());
    }

    @Test
    void shouldSuccessGetUserByName() {
        //given
        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        //when
        UserEntity userByName = underTest.getUserByName(user.getName());

        //then
        assertEquals(user, userByName);
        verify(userRepository, times(1)).findByName(user.getName());
    }

    @Test
    void shouldFailGetUserByNameIfUserWithThisNameIsNotRegistered() {
        //expected
        BusinessException ex = assertThrows(BusinessException.class, () -> underTest.getUserByName(user.getName()));

        //then
        assertEquals(String.format("User with name %s does not registered", user.getName()), ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verify(userRepository, times(1)).findByName(user.getName());
    }
}
