package com.jagdish.SocialMeet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSuggestionDTO {
    private Long id;
    private String name;
    private String username;
    private String avatarUrl;
}
