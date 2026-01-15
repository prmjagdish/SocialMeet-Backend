package com.jagdish.SocialMeet.model.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChatDto {
    private Long id;
    private String username;
    private String avatarUrl;
}
