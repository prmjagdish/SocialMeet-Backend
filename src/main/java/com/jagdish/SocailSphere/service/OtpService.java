package com.jagdish.SocailSphere.service;

public interface OtpService {
    String generateOtp();

    void sendOtp(String toEmail);

    boolean verifyOtp(String email, String otp);
}
