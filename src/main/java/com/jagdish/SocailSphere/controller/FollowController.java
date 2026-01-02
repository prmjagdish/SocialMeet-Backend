package com.jagdish.SocailSphere.controller;

import com.jagdish.SocailSphere.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class FollowController {

    private final UserServiceImpl userService;

    //follow user
    @PostMapping("/{username}/follow")
    public ResponseEntity<?> followUser(@PathVariable String username){
        userService.followUser(username);
        return ResponseEntity.ok("User followed successfully");
    }


    @PostMapping("/unfollow/{username}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String username) {
        userService.unfollowUser(username);
        return ResponseEntity.ok().build();
    }

}
