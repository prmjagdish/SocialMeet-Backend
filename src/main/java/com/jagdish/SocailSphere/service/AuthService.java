package com.jagdish.SocailSphere.service;

import com.jagdish.SocailSphere.model.dto.AuthRequest;
import com.jagdish.SocailSphere.model.dto.LoginRequest;

public interface AuthService {
    String register(AuthRequest request);

    String login(LoginRequest request);
}
