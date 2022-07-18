package com.inside.messagesystem.service;

import com.inside.messagesystem.dto.request.RegistrationRequestDto;
import com.inside.messagesystem.entity.UserEntity;

import java.sql.SQLException;

public interface UserService {

    void registration(RegistrationRequestDto dto) throws SQLException;

    UserEntity getUserFromToken(String token);

    UserEntity getUserByName(String name);
}
