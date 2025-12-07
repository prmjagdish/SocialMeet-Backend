package com.jagdish.SocailSphere.controller;
import com.jagdish.SocailSphere.model.dto.AuthRequest;
import com.jagdish.SocailSphere.model.dto.AuthResponse;
import com.jagdish.SocailSphere.model.dto.LoginRequest;
import com.jagdish.SocailSphere.model.dto.OtpRequest;
import com.jagdish.SocailSphere.service.impl.AuthServiceImpl;
import com.jagdish.SocailSphere.service.impl.OtpServiceImpl;
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
    public ResponseEntity<String> singUpUser(@RequestBody AuthRequest request) {
        String result = authServiceimpl.register(request);

        if (result.equals("User registered successfully.")) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        if (otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            return ResponseEntity.ok("{\"verified\": true}");
        } else {
            return ResponseEntity.status(400).body("{\"verified\": false}");
        }
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

}
