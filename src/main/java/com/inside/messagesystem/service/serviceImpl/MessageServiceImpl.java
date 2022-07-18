package com.inside.messagesystem.service.serviceImpl;

import com.inside.messagesystem.dto.request.MessageRequestDto;
import com.inside.messagesystem.entity.MessageEntity;
import com.inside.messagesystem.mapper.MessageListDtoMapper;
import com.inside.messagesystem.repository.MessageRepository;
import com.inside.messagesystem.service.MessageService;
import com.inside.messagesystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageListDtoMapper messageListDtoMapper;

    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, MessageListDtoMapper messageListDtoMapper) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messageListDtoMapper = messageListDtoMapper;
    }

    @Override
    public void saveMessage(MessageRequestDto dto) {
        MessageEntity message = new MessageEntity();
        message.setUser(userService.getUserByName(dto.getName()));
        message.setMessage(dto.getMessage());

        messageRepository.save(message);
    }

    @Override
    public String getLastMessages(MessageRequestDto dto) {
        return messageListDtoMapper.mapMessageListToDto(messageRepository.findLast10Messages());
    }
}
