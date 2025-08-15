package com.jagdish.SocailSphere.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostDto {
    private Long id;
    private String imageUrl;
    private String caption;
    private String username; // This is username
    private String avatarUrl;
    private Integer likes;
    private List<CommentDto> comments;

}
