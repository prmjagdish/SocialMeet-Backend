package com.jagdish.SocialMeet.service;
import com.jagdish.SocialMeet.model.dto.AuthRequest;

public interface OtpService {

    String generateOtp();
    void saveSignupAndSendOtp(AuthRequest request);
    boolean verifyOtpAndRegister(String email, String otp);
}
