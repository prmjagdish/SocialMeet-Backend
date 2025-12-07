package com.jagdish.SocailSphere.service;

import com.jagdish.SocailSphere.model.dto.EditProfileRequest;
import com.jagdish.SocailSphere.model.dto.UserDto;
import com.jagdish.SocailSphere.model.dto.UserProfileResponse;

public interface UserService {
    UserDto updateProfile(String username, EditProfileRequest request);
    UserProfileResponse getFullUserProfile(String username);

}
