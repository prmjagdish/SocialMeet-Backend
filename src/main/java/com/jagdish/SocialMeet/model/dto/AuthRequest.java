package com.jagdish.SocialMeet.model.dto;
import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String username;
    private String email;
    private String password;
}
