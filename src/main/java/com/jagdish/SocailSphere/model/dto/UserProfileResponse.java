package com.jagdish.SocailSphere.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private UserDto user;
    private List<PostDto> posts;
    private List<ReelDto> reels;
    private List<PostDto> savedPosts;
    private List<String> followers;
    private List<String> following;

}
