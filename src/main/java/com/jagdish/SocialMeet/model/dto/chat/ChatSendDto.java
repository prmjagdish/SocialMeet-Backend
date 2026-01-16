package com.jagdish.SocialMeet.model.dto.chat;

import lombok.Data;

@Data
public class ChatSendDto {
    private Long senderId;
    private Long receiverId;
    private String content;
}
