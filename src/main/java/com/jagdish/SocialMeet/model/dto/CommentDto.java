package com.jagdish.SocialMeet.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String username;
    private String avatarUrl;
    private LocalDateTime createdAt;

    public CommentDto(Long id, String content, String username) {
    }
}

