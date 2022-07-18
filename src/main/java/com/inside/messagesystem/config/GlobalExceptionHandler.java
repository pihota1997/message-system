package com.inside.messagesystem.config;

import com.inside.messagesystem.dto.response.ApiBusinessMessageDto;
import com.inside.messagesystem.exception.BusinessException;
import com.inside.messagesystem.exception.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiBusinessMessageDto> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiBusinessMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiBusinessMessageDto> handleErrorServerException() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiBusinessMessageDto("User already registered"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiBusinessMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiBusinessMessageDto(ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiBusinessMessageDto> handleBusinessException(SecurityException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ApiBusinessMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiBusinessMessageDto> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ApiBusinessMessageDto(ex.getMessage()));
    }
}
