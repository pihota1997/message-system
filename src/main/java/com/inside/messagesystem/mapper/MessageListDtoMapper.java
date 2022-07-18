package com.inside.messagesystem.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inside.messagesystem.dto.response.MessageResponseDto;
import com.inside.messagesystem.entity.MessageEntity;
import com.inside.messagesystem.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageListDtoMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JavaTimeModule timeModule = new JavaTimeModule();

    public String mapMessageListToDto(List<MessageEntity> messages) {
        try {
            List<MessageResponseDto> messageList = messages.stream().map(MessageResponseDto::new).collect(Collectors.toList());
            objectMapper.registerModule(timeModule);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(messageList);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error. Try  again later");
        }
    }
}
