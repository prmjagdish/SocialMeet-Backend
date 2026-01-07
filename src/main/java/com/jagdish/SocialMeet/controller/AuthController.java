package com.jagdish.SocialMeet.controller;
import com.jagdish.SocialMeet.model.dto.*;
import com.jagdish.SocialMeet.service.impl.AuthServiceImpl;
import com.jagdish.SocialMeet.service.impl.OtpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authServiceimpl;

    @Autowired
    private OtpServiceImpl otpService;


    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = authServiceimpl.isUsernameAvailable(username);

        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        otpService.saveSignupAndSendOtp(request);
        return ResponseEntity.ok("OTP sent successfully!");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        boolean verified = otpService.verifyOtpAndRegister(request.getEmail(), request.getOtp());

        if (!verified) {
            return ResponseEntity.status(400).body("{\"verified\": false}");
        }
        return ResponseEntity.ok("{\"verified\": true}");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = authServiceimpl.login(request);
            return ResponseEntity.ok(new AuthResponse("Login successful", token));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(ex.getMessage(), null));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @RequestBody GoogleAuthRequest request) {
        try {
            String jwt = authServiceimpl.googleLogin(request.getToken());
            return ResponseEntity.ok(new AuthResponse("Google login successful", jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Google authentication failed", null));
        }
    }

}
