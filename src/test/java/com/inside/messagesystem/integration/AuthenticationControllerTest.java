package com.inside.messagesystem.integration;

import com.inside.messagesystem.dto.request.LoginRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.inside.messagesystem.util.Endpoint.Url.LOGIN_ENDPOINT;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldSuccessLogin() throws Exception {
        //given
        LoginRequestDto dto = new LoginRequestDto(user.getName(), "password");
        when(userRepository.findByName(dto.getName())).thenReturn(Optional.of(user));

        //when
        ResultActions perform = performLoginRequest(dto);

        //then
        responseAssertions(perform);
        verify(userRepository, times(1)).findByName(dto.getName());
    }

    @Test
    void shouldFailLoginWithUnauthorizedUser() throws Exception {
        //given
        LoginRequestDto dto = new LoginRequestDto(user.getName(), "password");

        //when
        ResultActions perform = performLoginRequest(dto);

        //then
        failResponseAssertions(perform, "This user is not registered yet");
        verify(userRepository, times(1)).findByName(dto.getName());
    }

    @Test
    void shouldFailLoginWithIncorrectPassword() throws Exception {
        //given
        LoginRequestDto dto = new LoginRequestDto(user.getName(), "password1");
        when(userRepository.findByName(dto.getName())).thenReturn(Optional.of(user));

        //when
        ResultActions perform = performLoginRequest(dto);

        //then
        failResponseAssertions(perform, "Bad credentials");
        verify(userRepository, times(1)).findByName(dto.getName());
    }

    private ResultActions performLoginRequest(LoginRequestDto requestDto) throws Exception {
        return mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDto)));
    }

    private void responseAssertions(ResultActions perform) throws Exception {
        perform.andExpect(status().is(HttpStatus.OK.value()));
    }

    private void failResponseAssertions(ResultActions perform, String expectedMessage) throws Exception {
        perform.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.responseMessage", is(expectedMessage)));
    }
}
