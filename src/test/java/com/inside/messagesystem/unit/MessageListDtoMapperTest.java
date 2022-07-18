package com.inside.messagesystem.unit;

import com.inside.messagesystem.entity.MessageEntity;
import com.inside.messagesystem.entity.RoleEntity;
import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.mapper.MessageListDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class MessageListDtoMapperTest {

    @InjectMocks
    private MessageListDtoMapper underTest;

    private MessageEntity message;

    private Instant createAt;

    @BeforeEach
    void init() {
        UserEntity user = new UserEntity();
        RoleEntity role = new RoleEntity();
        user.setId(1);
        user.setName("user");
        user.setRoles(Set.of(role));

        message = new MessageEntity();
        message.setId(1);
        message.setMessage("message");
        message.setUser(user);

        createAt = Instant.now();
        message.setCreateAt(createAt);
    }

    @Test
    void shouldSuccessMapMessageListToDto() {
        //given
        List<MessageEntity> messages = List.of(message);

        //when
        String json = underTest.mapMessageListToDto(messages);

        //then
        assertEquals(String.format("[{\"id\":1,\"userName\":\"user\",\"message\":\"message\",\"createAt\":\"%s\"}]",
                parseInstant(createAt)), json);
    }

    @Test
    void shouldSuccessMapMessageListToDtoIfNoMessagesInDB() {
        //given
        List<MessageEntity> messages = new LinkedList<>();

        //when
        String json = underTest.mapMessageListToDto(messages);

        //then
        assertEquals("[]", json);
    }

    private String parseInstant(Instant instant) {
        return instant.toString().split("T")[0] + " " + instant.toString().split("T")[1].split("\\.")[0];
    }
}
