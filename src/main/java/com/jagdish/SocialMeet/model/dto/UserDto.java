package com.jagdish.SocialMeet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String bio;
    private String avatar;
}
