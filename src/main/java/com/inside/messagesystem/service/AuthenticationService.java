package com.inside.messagesystem.service;

import com.inside.messagesystem.dto.request.LoginRequestDto;

public interface AuthenticationService {

    String login(LoginRequestDto dto);
}
