package com.inside.messagesystem.unit;

import com.inside.messagesystem.dto.request.MessageRequestDto;
import com.inside.messagesystem.entity.MessageEntity;
import com.inside.messagesystem.exception.BusinessException;
import com.inside.messagesystem.mapper.MessageListDtoMapper;
import com.inside.messagesystem.repository.MessageRepository;
import com.inside.messagesystem.service.UserService;
import com.inside.messagesystem.service.serviceImpl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private MessageListDtoMapper messageListDtoMapper;

    @InjectMocks
    private MessageServiceImpl underTest;

    @Test
    void shouldSuccessSaveMessage() {
        //given
        MessageRequestDto dto = new MessageRequestDto("user", "message");
        when(userService.getUserByName(dto.getName())).thenReturn(any());

        //when
        underTest.saveMessage(dto);

        //then
        verify(userService, times(1)).getUserByName(dto.getName());
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void shouldFailSaveMessageIfUserFromDtoIsNotRegistered() {
        //given
        MessageRequestDto dto = new MessageRequestDto("user", "message");
        when(userService.getUserByName(dto.getName())).thenThrow(BusinessException.class);

        //expected
        assertThrows(BusinessException.class, () -> underTest.saveMessage(dto));

        //then
        verify(userService, times(1)).getUserByName(dto.getName());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void shouldSuccessGetLastMessages() {
        //given
        MessageRequestDto dto = new MessageRequestDto("user", "history 10");
        List<MessageEntity> messages = new LinkedList<>();
        messages.add(new MessageEntity());
        when(messageRepository.findLast10Messages()).thenReturn(messages);
        when(messageListDtoMapper.mapMessageListToDto(any())).thenReturn("responseMessage");

        //when
        underTest.getLastMessages(dto);

        //then
        verify(messageRepository, times(1)).findLast10Messages();
        verify(messageListDtoMapper, times(1)).mapMessageListToDto(any());
    }
}
