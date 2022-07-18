package com.inside.messagesystem.unit;

import com.inside.messagesystem.dto.request.LoginRequestDto;
import com.inside.messagesystem.security.JwtTokenUtil;
import com.inside.messagesystem.service.serviceImpl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationServiceImpl underTest;

    @Test
    void shouldSuccessLogin() {
        //given
        LoginRequestDto dto = new LoginRequestDto("user", "password");
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn("token");

        //when
        underTest.login(dto);

        //then
        verify(authenticationProvider, times(1)).authenticate(any());
        verify(jwtTokenUtil, times(1)).generateAccessToken(any());
    }

    @Test
    void shouldFailLoginIfBadCredentials() {
        //given
        final LoginRequestDto dto = new LoginRequestDto("user", "password");
        when(authenticationProvider.authenticate(any())).thenThrow(BadCredentialsException.class);

        //expect
        assertThrows(BadCredentialsException.class, () -> underTest.login(dto));

        //then
        verify(authenticationProvider, times(1)).authenticate(any());
        verify(jwtTokenUtil, never()).generateAccessToken(any());
    }
}
