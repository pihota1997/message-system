package com.inside.messagesystem.service;

import com.inside.messagesystem.dto.request.MessageRequestDto;

public interface MessageService {

    void saveMessage(MessageRequestDto dto);

    String getLastMessages(MessageRequestDto dto);

}
