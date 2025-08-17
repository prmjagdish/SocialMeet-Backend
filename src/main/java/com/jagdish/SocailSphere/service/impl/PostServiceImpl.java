package com.jagdish.SocailSphere.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jagdish.SocailSphere.exception.PostNotFoundException;
import com.jagdish.SocailSphere.exception.UnauthorizedActionException;
import com.jagdish.SocailSphere.model.dto.CommentDto;
import com.jagdish.SocailSphere.model.dto.PostDto;
import com.jagdish.SocailSphere.model.entity.Post;
import com.jagdish.SocailSphere.model.entity.User;
import com.jagdish.SocailSphere.repository.PostRepository;
import com.jagdish.SocailSphere.repository.UserRepository;
import com.jagdish.SocailSphere.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;

    @Autowired
    private final PostRepository postRepository;

    private final Cloudinary cloudinary;

    @Override
    public PostDto createPost(String username, MultipartFile media, String caption) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Map uploadResult = cloudinary.uploader().upload(media.getBytes(),
                ObjectUtils.asMap("folder", "posts"));

        String uploadedUrl = (String) uploadResult.get("secure_url");

        Post post = new Post();
        log.info("Before setting values: {}", post);
        post.setImageUrl(uploadedUrl);
        post.setCaption(caption);
        post.setUser(user);
        log.info("After setting values: {}", post);
        System.out.println(post);
        postRepository.save(post);
        log.info("Saved Post -> id: {}, createdAt: {}", post.getId(), post.getCreatedAt());
        user.getPosts().add(post);

        PostDto responseDto = new PostDto();
        responseDto.setId(post.getId());
        responseDto.setImageUrl(uploadedUrl);
        responseDto.setCaption(post.getCaption());
        responseDto.setUsername(user.getUsername());
        responseDto.setAvatarUrl(user.getAvatarUrl());

        responseDto.setLikes(0);

        return responseDto;
    }

    @Override
    public List<PostDto> getHomeFeed(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return posts.stream().map(post -> {

//            System.out.println("Post Entity -> id: " + post.getId() +
//                    ", createdAt: " + post.getCreatedAt());

            PostDto dto = new PostDto();
            dto.setId(post.getId());
            dto.setImageUrl(post.getImageUrl());
            dto.setCaption(post.getCaption());
            dto.setUsername(post.getUser().getUsername());
            dto.setAvatarUrl(post.getUser().getAvatarUrl());
            dto.setLikes(post.getLikes());

            dto.setComments(
                    post.getComments().stream()
                            .map(comment -> new CommentDto(
                                    comment.getId(),
                                    comment.getContent(),
                                    comment.getUser().getUsername(),
                                    comment.getUser().getAvatarUrl(),
                                    comment.getCreatedAt()
                            ))
                            .collect(Collectors.toList())
            );

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if (!post.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }
}
