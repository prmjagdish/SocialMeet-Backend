package com.jagdish.SocialMeet.model.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private Long id;
    private String type;
    private List<UserChatDto> participants;
}
