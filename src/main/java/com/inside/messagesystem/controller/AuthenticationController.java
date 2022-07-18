package com.inside.messagesystem.controller;

import com.inside.messagesystem.dto.request.LoginRequestDto;
import com.inside.messagesystem.dto.response.TokenResponseDto;
import com.inside.messagesystem.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.inside.messagesystem.util.Endpoint.Url.LOGIN_ENDPOINT;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(LOGIN_ENDPOINT)
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {

        return ResponseEntity.ok().body(new TokenResponseDto(authenticationService.login(dto)));
    }
}
