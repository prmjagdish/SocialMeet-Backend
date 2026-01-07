package com.jagdish.SocialMeet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDto {
    private Long id;
    private String imageUrl;
    private String caption;
    private String username;
    private String avatarUrl;
    private Integer likes;
    private List<CommentDto> comments;
    private boolean isFollowingByMe;
}
