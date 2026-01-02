package com.jagdish.SocailSphere.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {
    private Long id;
    private String username;
    private String name;
    private String avatarUrl;
    private boolean isFollowing; // for follow button inside list
}
