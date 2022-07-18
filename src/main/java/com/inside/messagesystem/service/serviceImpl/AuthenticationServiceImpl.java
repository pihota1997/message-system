package com.inside.messagesystem.service.serviceImpl;

import com.inside.messagesystem.dto.request.LoginRequestDto;
import com.inside.messagesystem.security.JwtTokenUtil;
import com.inside.messagesystem.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationServiceImpl(AuthenticationProvider authenticationProvider, JwtTokenUtil jwtTokenUtil) {
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String login(LoginRequestDto dto) {
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(dto.getName(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateAccessToken(authentication);
    }
}