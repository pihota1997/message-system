package com.inside.messagesystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inside.messagesystem.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MessageResponseDto {

    private long id;
    private String userName;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+3")
    private Instant createAt;

    public MessageResponseDto(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.userName = messageEntity.getUser().getName();
        this.message = messageEntity.getMessage();
        this.createAt = messageEntity.getCreateAt();
    }
}