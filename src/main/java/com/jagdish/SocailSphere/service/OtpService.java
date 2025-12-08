package com.jagdish.SocailSphere.service;
import com.jagdish.SocailSphere.model.dto.AuthRequest;

public interface OtpService {

    String generateOtp();
    void saveSignupAndSendOtp(AuthRequest request);
    boolean verifyOtpAndRegister(String email, String otp);
}
