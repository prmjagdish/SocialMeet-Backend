package com.jagdish.SocailSphere.controller;
import com.jagdish.SocailSphere.model.dto.PostDto;
import com.jagdish.SocailSphere.model.entity.Post;
import com.jagdish.SocailSphere.model.entity.User;
import com.jagdish.SocailSphere.repository.PostRepository;
import com.jagdish.SocailSphere.repository.UserRepository;
import com.jagdish.SocailSphere.service.impl.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{username}")
    public ResponseEntity<PostDto> createPost(@PathVariable String username, @RequestParam("media") MultipartFile media,  @RequestParam("caption") String caption) throws Exception {
        return ResponseEntity.ok(postService.createPost(username, media, caption));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDto>> getHomeFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostDto> feed = postService.getHomeFeed(page, size);
        return ResponseEntity.ok(feed);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto dto) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setImageUrl(dto.getImageUrl());
        post.setCaption(dto.getCaption());
        postRepository.save(post);
        dto.setId(post.getId());
        return ResponseEntity.ok(dto);
    }

    // ✅ DELETE POST
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return ResponseEntity.noContent().build();
    }

    // ✅ SAVE POST
    @PutMapping("/save/{username}/{postId}")
    public ResponseEntity<Void> savePost(@PathVariable String username, @PathVariable Long postId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        user.getSavedPosts().add(post);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // ✅ UNSAVE POST
    @PutMapping("/unsave/{username}/{postId}")
    public ResponseEntity<Void> unsavePost(@PathVariable String username, @PathVariable Long postId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        user.getSavedPosts().remove(post);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
