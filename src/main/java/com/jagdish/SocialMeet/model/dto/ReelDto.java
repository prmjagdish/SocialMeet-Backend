package com.jagdish.SocialMeet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
    public class ReelDto {
        private Long id;
        private String caption;
        private String videoUrl;
    }
