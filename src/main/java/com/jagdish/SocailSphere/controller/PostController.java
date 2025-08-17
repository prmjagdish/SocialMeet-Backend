package com.jagdish.SocailSphere.controller;

import com.jagdish.SocailSphere.model.dto.PostDto;
import com.jagdish.SocailSphere.repository.PostRepository;
import com.jagdish.SocailSphere.repository.UserRepository;
import com.jagdish.SocailSphere.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Autowired
    private UserRepository userRepository;

    // Create post
    @PostMapping("/{username}")
    public ResponseEntity<PostDto> createPost(@PathVariable String username, @RequestParam("media") MultipartFile media, @RequestParam("caption") String caption) throws Exception {
        return ResponseEntity.ok(postServiceImpl.createPost(username, media, caption));
    }

    // Get all posts for homepage
    @GetMapping("/feed")
    public ResponseEntity<List<PostDto>> getHomeFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostDto> feed = postServiceImpl.getHomeFeed(page, size);
        return ResponseEntity.ok(feed);
    }

    // delete post by id and by owner of post
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             @RequestParam String username) {
        postServiceImpl.deletePost(postId, username);
        return ResponseEntity.ok("Post Delete success");
    }

}
