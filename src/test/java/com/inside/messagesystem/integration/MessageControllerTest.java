package com.inside.messagesystem.integration;

import com.inside.messagesystem.dto.request.MessageRequestDto;
import com.inside.messagesystem.entity.MessageEntity;
import com.inside.messagesystem.entity.RoleEntity;
import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static com.inside.messagesystem.util.Endpoint.Url.MESSAGE_ENDPOINT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends AbstractIntegrationTest {

    @Value("${jwt.token.access.expiration}")
    private int accessTokenExpirationTime;

    private MessageEntity message;

    private Instant createAt;

    @BeforeEach
    void initForMessageControllerTest() {
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
    void shouldSuccessReturnMessages() throws Exception {
        //given
        MessageRequestDto dto = new MessageRequestDto(user.getName(), "history 10");
        List<MessageEntity> messages = List.of(message);

        when(userRepository.findByName(dto.getName())).thenReturn(Optional.of(user));
        when(messageRepository.findLast10Messages()).thenReturn(messages);

        //when
        final ResultActions perform = performMessageRequest(dto);

        //then
        responseAssertions(perform, HttpStatus.OK, String.format("[{\"id\":1,\"userName\":\"user\",\"message\":\"message\",\"createAt\":\"%s\"}]",
                parseInstant(createAt)));
        verify(userRepository, times(1)).findByName(dto.getName());
        verify(messageRepository, times(1)).findLast10Messages();
    }

    @Test
    void shouldSuccessReturnMessagesIfNoMessagesInDB() throws Exception {
        //given
        MessageRequestDto dto = new MessageRequestDto(user.getName(), "history 10");

        when(userRepository.findByName(dto.getName())).thenReturn(Optional.of(user));

        //when
        final ResultActions perform = performMessageRequest(dto);

        //then
        responseAssertions(perform, HttpStatus.OK, "[]");
        verify(userRepository, times(1)).findByName(dto.getName());
        verify(messageRepository, times(1)).findLast10Messages();
    }

    @Test
    void shouldFailReturnMessagesIfUserUnauthorized() throws Exception {
        MessageRequestDto dto = new MessageRequestDto(user.getName(), "history 10");

        //when
        final ResultActions perform = performMessageRequestWithoutAuthorization(dto);

        //then
        responseAssertions(perform, HttpStatus.UNAUTHORIZED, "");
        verify(userRepository, never()).findByName(dto.getName());
        verify(messageRepository, never()).findLast10Messages();
    }

    @Test
    void shouldSuccessSaveMessage() throws Exception {
        //given
        MessageRequestDto dto = new MessageRequestDto(user.getName(), "message");

        when(userRepository.findByName(dto.getName())).thenReturn(Optional.of(user));

        //when
        final ResultActions perform = performMessageRequest(dto);

        //then
        responseWithApiBusinessMessageAssertions(perform, "Message saved successfully");
        verify(userRepository, atLeastOnce()).findByName(dto.getName());
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void shouldFailSaveMessageIfUserUnauthorized() throws Exception {
        //given
        MessageRequestDto dto = new MessageRequestDto(user.getName(), "message");

        //when
        final ResultActions perform = performMessageRequestWithoutAuthorization(dto);

        //then
        responseAssertions(perform, HttpStatus.UNAUTHORIZED, "");
        verify(userRepository, never()).findByName(dto.getName());
        verify(messageRepository, never()).save(any());
    }

    private ResultActions performMessageRequest(MessageRequestDto dto) throws Exception {
        return mockMvc.perform(post(MESSAGE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto))
                .header("Authorization", "Bearer_" + generateAccessTokenFromTest(user, accessTokenExpirationTime)));
    }

    private ResultActions performMessageRequestWithoutAuthorization(MessageRequestDto dto) throws Exception {
        return mockMvc.perform(post(MESSAGE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto)));
    }

    private void responseAssertions(ResultActions perform, HttpStatus expectedStatus, String expectedMessage) throws Exception {
        perform.andExpect(status().is(expectedStatus.value()))
                .andExpect(content().string(expectedMessage));
    }

    private void responseWithApiBusinessMessageAssertions(ResultActions perform, String expectedMessage) throws Exception {
        perform.andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.responseMessage", is(expectedMessage)));
    }

    private String parseInstant(Instant instant) {
        return instant.toString().split("T")[0] + " " + instant.toString().split("T")[1].split("\\.")[0];
    }
}
