package com.jagdish.SocialMeet.model.dto;

import lombok.Data;

@Data
public class EditProfileRequest {
    private String name;
    private String bio;
    private String avatar;
}
