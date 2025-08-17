package com.jagdish.SocailSphere.service;

import com.jagdish.SocailSphere.model.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto createPost(String username, MultipartFile media, String caption) throws IOException;

    List<PostDto> getHomeFeed(int page, int size);

    void deletePost(Long postId, String username);
}