package com.jagdish.SocailSphere.service;

import com.jagdish.SocailSphere.model.dto.EditProfileRequest;
import com.jagdish.SocailSphere.model.dto.UserDto;
import com.jagdish.SocailSphere.model.dto.UserProfileResponse;
import com.jagdish.SocailSphere.model.dto.UserSuggestionDTO;

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
