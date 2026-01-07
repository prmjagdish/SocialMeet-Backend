package com.jagdish.SocialMeet.service;
import com.jagdish.SocialMeet.model.dto.LoginRequest;
import com.jagdish.SocialMeet.model.entity.User;

public interface AuthService {
    boolean isUsernameAvailable(String username);
    String login(LoginRequest request);
    User getCurrentUser();
    String googleLogin(String token);
}
