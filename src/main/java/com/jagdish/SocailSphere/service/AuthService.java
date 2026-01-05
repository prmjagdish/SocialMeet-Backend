package com.jagdish.SocailSphere.service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.jagdish.SocailSphere.model.dto.LoginRequest;
import com.jagdish.SocailSphere.model.entity.User;

public interface AuthService {
    boolean isUsernameAvailable(String username);
    String login(LoginRequest request);
    User getCurrentUser();
    String googleLogin(String token);
}
