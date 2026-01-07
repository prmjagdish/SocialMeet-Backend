package com.jagdish.SocialMeet.service;

import com.jagdish.SocialMeet.model.dto.EditProfileRequest;
import com.jagdish.SocialMeet.model.dto.UserDto;
import com.jagdish.SocialMeet.model.dto.UserProfileResponse;
import com.jagdish.SocialMeet.model.dto.UserSuggestionDTO;

import java.util.List;

public interface UserService {
    UserDto updateProfile(String username, EditProfileRequest request);
    UserProfileResponse getFullUserProfile(String username);
    List<UserSuggestionDTO> getSuggestedUsers(Long userId);
    void deleteCurrentUser();
    UserProfileResponse getPublicUserProfile(String username);
    void followUser(String username);
    void unfollowUser(String username);
}
