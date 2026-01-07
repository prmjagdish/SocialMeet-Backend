package com.jagdish.SocialMeet.service.impl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jagdish.SocialMeet.model.dto.LoginRequest;
import com.jagdish.SocialMeet.model.entity.User;
import com.jagdish.SocialMeet.repository.UserRepository;
import com.jagdish.SocialMeet.service.AuthService;
import com.jagdish.SocialMeet.utilies.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;


    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }


    @Override
    public String googleLogin(String token) {

        GoogleIdToken.Payload payload = verifyGoogleToken(token);

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAvatarUrl(picture);
            user.setUsername(email.split("@")[0]);
            user.setPassword(null); // NO password for Google users
            user.setProvider("GOOGLE");

            userRepository.save(user);
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    private GoogleIdToken.Payload verifyGoogleToken(String token) {

        try {
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance()
                    )
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
