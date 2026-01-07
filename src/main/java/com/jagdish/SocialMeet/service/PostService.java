package com.jagdish.SocialMeet.service;

import com.jagdish.SocialMeet.model.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto createPost(String username, MultipartFile media, String caption) throws IOException;

    List<PostDto> getHomeFeed(int page, int size);

    void deletePost(Long postId, String username);
}