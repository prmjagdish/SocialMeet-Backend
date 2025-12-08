package com.jagdish.SocailSphere.service;
import com.jagdish.SocailSphere.model.dto.LoginRequest;

public interface AuthService {
    boolean isUsernameAvailable(String username);
    String login(LoginRequest request);
}
