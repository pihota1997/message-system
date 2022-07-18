package com.inside.messagesystem.controller;

import com.inside.messagesystem.dto.request.RegistrationRequestDto;
import com.inside.messagesystem.dto.response.ApiBusinessMessageDto;
import com.inside.messagesystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLException;

import static com.inside.messagesystem.util.Endpoint.Url.REGISTRATION_ENDPOINT;

@RestController
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(REGISTRATION_ENDPOINT)
    public ResponseEntity<ApiBusinessMessageDto> registration(@Valid @RequestBody RegistrationRequestDto dto) throws SQLException {

        userService.registration(dto);
        return ResponseEntity.ok().body(new ApiBusinessMessageDto(String.format("User with name: %s added", dto.getName())));
    }
}
