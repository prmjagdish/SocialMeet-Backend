package com.jagdish.SocialMeet.service.impl;

import com.jagdish.SocialMeet.model.dto.*;
import com.jagdish.SocialMeet.model.entity.Post;
import com.jagdish.SocialMeet.model.entity.Reel;
import com.jagdish.SocialMeet.model.entity.User;
import com.jagdish.SocialMeet.repository.PostRepository;
import com.jagdish.SocialMeet.repository.ReelRepository;
import com.jagdish.SocialMeet.repository.UserRepository;
import com.jagdish.SocialMeet.service.UserService;
import com.jagdish.SocialMeet.utilies.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReelRepository reelRepository;

    private final AuthUtil authUtil;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public UserDto updateProfile(String username, EditProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setAvatarUrl(request.getAvatar());

        userRepository.save(user);

        return mapToDto(user);
    }

    @Override
    public UserProfileResponse getFullUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map posts by this user
        List<PostDto> posts = postRepository.findByUser(user).stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());

        // Map reels by this user
        List<ReelDto> reels = reelRepository.findByUser(user).stream()
                .map(this::mapToReelDto)
                .collect(Collectors.toList());

        // Map saved posts (if your User entity has savedPosts relationship)
        List<PostDto> savedPosts = user.getSavedPosts() != null ?
                user.getSavedPosts().stream()
                        .map(this::mapToPostDto)
                        .collect(Collectors.toList())
                : List.of();

        // Followers & Following usernames
        List<UserSummaryDto> followers = user.getFollowers().stream()
                .map(u -> mapToUserSummary(u, user))
                .toList();

        List<UserSummaryDto> following = user.getFollowing().stream()
                .map(u -> mapToUserSummary(u,user))
                .toList();

        // Construct the full response
        UserProfileResponse response = new UserProfileResponse();
        response.setOwner(true);
        response.setUser(mapToDto(user));
        response.setPosts(posts);
        response.setReels(reels);
        response.setSavedPosts(savedPosts);
        response.setFollowers(followers);
        response.setFollowing(following);
        return response;
    }

    @Override
    @Transactional
    public void followUser(String username){
        User currentUser = authUtil.getCurrentUser();

        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(currentUser.getId().equals(targetUser.getId())){
            throw new RuntimeException("You cant not follow yourself");
        }

        if(!currentUser.getFollowing().contains(targetUser)){
            currentUser.getFollowing().add(targetUser);
            targetUser.getFollowers().add(currentUser);
        }

        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }

    @Override
    @Transactional
    public void unfollowUser(String username) {
        User currentUser = authUtil.getCurrentUser();

        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new RuntimeException("You cannot unfollow yourself");
        }

        if (currentUser.getFollowing().contains(targetUser)) {
            currentUser.getFollowing().remove(targetUser);
            targetUser.getFollowers().remove(currentUser);
        }

        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }


    // Map Post entity to PostDto
    private PostDto mapToPostDto(Post post) {

        User currentUser = authUtil.getCurrentUser();
        User postOwner = post.getUser();

        boolean isFollowing = currentUser.getFollowing()
                .stream()
                .anyMatch(u -> u.getId().equals(postOwner.getId()));

        return new PostDto(
                post.getId(),
                post.getImageUrl(),
                post.getCaption(),
                postOwner.getUsername(),
                postOwner.getAvatarUrl(),
                post.getLikedByUsers().size(),
                post.getComments().stream()
                        .map(comment -> new CommentDto(
                                comment.getId(),
                                comment.getContent(),
                                comment.getUser().getUsername()
                        ))
                        .collect(Collectors.toList()),
                isFollowing // ✅ THIS FIXES REFRESH ISSUE
        );
    }


    // Map Reel entity to ReelDto
    private ReelDto mapToReelDto(Reel reel) {
        return new ReelDto(
                reel.getId(),
                reel.getVideoUrl(),
                reel.getCaption()
        );
    }

    // Map User entity to UserDto
    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setBio(user.getBio());
        dto.setAvatar(user.getAvatarUrl());
        return dto;
    }
    private UserSummaryDto mapToUserSummary(User targetUser, User currentUser) {
        boolean isFollowing = currentUser.getFollowing()
                .stream()
                .anyMatch(u -> u.getId().equals(targetUser.getId()));

        return new UserSummaryDto(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getName(),
                targetUser.getAvatarUrl(),
                isFollowing
        );
    }


    public List<UserSuggestionDTO> getSuggestedUsers(Long userId) {
        return userRepository.findSuggestedUsers(userId)
                .stream()
                .map(u -> new UserSuggestionDTO(
                        u.getId(),
                        u.getName(),
                        "@" + u.getUsername(),
                        u.getAvatarUrl()

                ))
                .toList();
    }

    @Override
    @Transactional
    public void deleteCurrentUser() {
        User user = authUtil.getCurrentUser();
        userRepository.delete(user);
    }

    @Override
    public UserProfileResponse getPublicUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = authService.getCurrentUser();

        List<PostDto> posts = postRepository.findByUser(user).stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());

        List<ReelDto> reels = reelRepository.findByUser(user).stream()
                .map(this::mapToReelDto)
                .collect(Collectors.toList());

        List<UserSummaryDto> followers = user.getFollowers().stream()
                .map(u -> mapToUserSummary(u, user))
                .toList();

        List<UserSummaryDto> following = user.getFollowing().stream()
                .map(u -> mapToUserSummary(u,user))
                .toList();

        boolean isFollowing = currentUser != null && user.getFollowers().contains(currentUser);

        UserProfileResponse response = new UserProfileResponse();
        response.setUser(mapToDto(user));
        response.setPosts(posts);
        response.setReels(reels);
        response.setFollowers(followers);
        response.setFollowing(following);
        response.setFollowingByMe(isFollowing);

        // ❌ Do NOT expose saved posts
//        response.setSavedPosts(List.of());

        return response;
    }

}
