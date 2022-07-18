package com.inside.messagesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessagesListResponseDto {

    private List<MessageResponseDto> messages;
}