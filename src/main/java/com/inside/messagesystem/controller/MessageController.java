package com.inside.messagesystem.controller;

import com.inside.messagesystem.dto.request.MessageRequestDto;
import com.inside.messagesystem.dto.response.ApiBusinessMessageDto;
import com.inside.messagesystem.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.inside.messagesystem.util.Endpoint.Url.MESSAGE_ENDPOINT;

@RestController
public class MessageController {

    private final MessageService messageService;
    private static final String GET_MESSAGES_TEXT = "history 10";

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(MESSAGE_ENDPOINT)
    public ResponseEntity<?> message(@RequestBody MessageRequestDto dto) {

        if (dto.getMessage().equals(GET_MESSAGES_TEXT)) {
            return ResponseEntity.ok()
                    .header("Content-type", "application/json;charset=UTF-8")
                    .body(messageService.getLastMessages(dto));
        }

        messageService.saveMessage(dto);
        return ResponseEntity.ok().body(new ApiBusinessMessageDto("Message saved successfully"));
    }
}
