package com.jagdish.SocailSphere.controller;
import com.jagdish.SocailSphere.model.dto.EditProfileRequest;
import com.jagdish.SocailSphere.model.dto.UserDto;
import com.jagdish.SocailSphere.model.dto.UserProfileResponse;
import com.jagdish.SocailSphere.model.dto.UserSuggestionDTO;
import com.jagdish.SocailSphere.service.impl.UserServiceImpl;
import com.jagdish.SocailSphere.utilies.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private  JwtUtil jwtUtil;

    @GetMapping("/me")
    public UserProfileResponse getUserProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        return userService.getFullUserProfile(username);
    }

    @PutMapping("/{username}/edit")
    public UserDto updateUserProfile( @PathVariable String username, @RequestBody EditProfileRequest request) {
        return userService.updateProfile(username, request);
    }

    @GetMapping("/suggested")
    public List<UserSuggestionDTO> getSuggestedUsers(
            @RequestParam Long userId
    ) {
        return userService.getSuggestedUsers(userId);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyAccount() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok(
                Map.of("message", "Account permanently deleted")
        );
    }
}
